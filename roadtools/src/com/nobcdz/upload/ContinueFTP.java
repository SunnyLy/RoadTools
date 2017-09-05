package com.nobcdz.upload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ContinueFTP {
	public FTPClient ftpClient = new FTPClient();
	public Context context;

	public ContinueFTP(Context context) {

		this.context = context;
		this.ftpClient.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));
	}

	public boolean connect(String hostname, int port, String username,
			String password) throws IOException {

		ftpClient.connect(hostname, port);
		ftpClient.setControlEncoding("UTF-8");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				return true;
			}
		}
		disconnect();
		return false;
	}

	public UploadStatus uploadTrack(String local, String remote)
			throws IOException {
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("UTF-8");
		String remoteFileName = remote;
		File localfile = new File(local);
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
				return UploadStatus.Create_Directory_Fail;
			}
		}

		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName
				.getBytes("UTF-8"), "iso-8859-1"));
		if (files.length == 1) { // 文件存在
			long remoteSize = files[0].getSize();
			long localSize = localfile.length();
			if (remoteSize == localSize) {
				return UploadStatus.File_Exits;
			} else if (remoteSize > localSize) {
				return UploadStatus.Remote_Bigger_Local;
			} else if (!ftpClient.deleteFile(remoteFileName)) {
				return UploadStatus.Delete_Remote_Faild;
			}
		}
		return uploadFile(remoteFileName, new File(local), ftpClient, 0);
	}

	public UploadStatus upload(String local, String remote) throws IOException {
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("UTF-8");
		UploadStatus result;
		String remoteFileName = remote;
		File localfile = new File(local);
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
				return UploadStatus.Create_Directory_Fail;
			}
		}

		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName
				.getBytes("UTF-8"), "iso-8859-1"));

		if (files.length == 1) { // 文件存在
			long remoteSize = files[0].getSize();
			long localSize = localfile.length();

			if (remoteSize == localSize) {
				return UploadStatus.File_Exits;
			} else if (remoteSize > localSize) {
				return UploadStatus.Remote_Bigger_Local;
			}

			result = uploadFile(remoteFileName, localfile, ftpClient,
					remoteSize);

			if (result == UploadStatus.Upload_From_Break_Failed) {
				if (!ftpClient.deleteFile(remoteFileName)) {
					return UploadStatus.Delete_Remote_Faild;
				}
				result = uploadFile(remoteFileName, localfile, ftpClient, 0);
			}
		} else {
			result = uploadFile(remoteFileName, localfile, ftpClient, 0);
		}
		return result;
	}

	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	public UploadStatus CreateDirecroty(String remote, FTPClient ftpClient)
			throws IOException {
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory
						.getBytes("UTF-8"), "iso-8859-1"))) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end)
						.getBytes("UTF-8"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录失败");
						return UploadStatus.Create_Directory_Fail;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	public UploadStatus uploadFile(String remoteFile, File localFile,
			FTPClient ftpClient, long remoteSize) throws IOException {
		UploadStatus status;
		long step = localFile.length() / 100;
		long process = 0;
		long localreadbytes = 0L;
		RandomAccessFile raf = new RandomAccessFile(localFile, "r");
		OutputStream out = ftpClient.appendFileStream(new String(remoteFile
				.getBytes("UTF-8"), "iso-8859-1"));
		// OutputStream out = ftpClient.storeFileStream(new String(remoteFile
		// .getBytes("UTF-8"), "iso-8859-1"));

		if (remoteSize > 0) {
			ftpClient.setRestartOffset(remoteSize);
			process = remoteSize / step;
			raf.seek(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] bytes = new byte[1024];
		int c;
		while ((c = raf.read(bytes)) != -1) {
			Log.i("process", "process:tag:" + 1);

			out.write(bytes, 0, c);

			Log.i("process", "process:tag:" + 2);
			localreadbytes += c;
			Log.i("process", "process:tag:" + 3);
			if (localreadbytes / step != process) {
				process = localreadbytes / step;
				sendMsg(process);
			}
			Log.i("process", "process:" + localreadbytes);
		}
		out.flush();
		raf.close();
		out.close();
		boolean result = ftpClient.completePendingCommand();
		if (remoteSize > 0) {
			status = result ? UploadStatus.Upload_From_Break_Success
					: UploadStatus.Upload_From_Break_Failed;
		} else {
			status = result ? UploadStatus.Upload_New_File_Success
					: UploadStatus.Upload_New_File_Failed;
		}
		return status;
	}

	private void sendMsg(long length) {
		Intent intent = new Intent("com.android.myborder");
		intent.putExtra("msg", (int) length);
		context.sendBroadcast(intent);
	}
}