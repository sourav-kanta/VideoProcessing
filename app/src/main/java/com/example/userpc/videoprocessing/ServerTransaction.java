package com.example.userpc.videoprocessing;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTransaction extends Thread{
	
	private Socket socket=null;
	private Context context;
	ArrayList<File> files;
	int size;
	
	public ServerTransaction(Socket soc, Context c,ArrayList<File> fin)
	{
		socket=soc;
		context=c;
		files=fin;
		size=files.size();

	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Log.e("Sever", "Transaction called");
		if(socket==null)
			return;
		
		/*
		 * Main Protocol for server-side transfer
		 */
		try
		{
			InputStream in=socket.getInputStream();
			OutputStream out=socket.getOutputStream();
			DataInputStream din=new DataInputStream(in);
			DataOutputStream dout=new DataOutputStream(out);
			dout.writeUTF(size+"");
			int i=0;
			FileInputStream fstream;
			byte[] bytes=new byte[16*1024];
			while(i<size)
			{
				dout.writeUTF(files.get(i).getName());
				dout.writeLong(files.get(i).length());
				int count;
				fstream=new FileInputStream(files.get(i));
				while ((count = fstream.read(bytes)) >= 0) {
					out.write(bytes, 0, count);
				}
				fstream.close();
				i++;
				dout.flush();
			}
			din.close();
			dout.close();
			in.close();
			out.close();
			socket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.e("Server Error", "In main transaction block");
			return;
		}
		Log.e("Send", "Complete");
	}

}
