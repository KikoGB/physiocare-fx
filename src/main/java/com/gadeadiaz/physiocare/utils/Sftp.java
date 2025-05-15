package com.gadeadiaz.physiocare.utils;

import com.jcraft.jsch.*;
import io.github.cdimascio.dotenv.Dotenv;

public class Sftp {
    public static void savePDF(String localFilePath, String nameInServer){
        JSch jsch = new JSch();

        try{
            Dotenv dotenv = Dotenv.load();
            String host = dotenv.get("SFTP_HOST");
            String user = dotenv.get("SFTP_USER");
            String pass = dotenv.get("SFTP_PASS");
            Session session = jsch.getSession(user,host);
            session.setPassword(pass);

            //Configuration to not validate the host key
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            System.out.println("SFTP connection established");

            //Establish connection
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel; //Open channel cominications

            sftpChannel.put(localFilePath, nameInServer); //Save pdf
            System.out.println("Pdf Saved with Sftp protocol");

            // Close connection
            if (sftpChannel.isConnected()){
                sftpChannel.exit();
                session.disconnect();
                System.out.println("SFTP Connection are closed!");
            }

        }catch(JSchException jse){
            System.out.println("Error connecting to server: " + jse.getMessage());
        }catch (SftpException sftpe) {
            System.out.println("Error uploading pdf with SFTP protocol: " + sftpe.getMessage());
        }
    }
}
