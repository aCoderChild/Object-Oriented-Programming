package com.example.my_group_project.Controllers.User;
import com.example.my_group_project.SoundPlay;

import com.example.my_group_project.Controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class UserSendEmailController extends BaseController {
    @FXML
    private TextField otpcode;

    private String OTPcode;

    @FXML
    public void initialize() {
        sendOTP(getUserEmail());
    }

    private String getUserEmail() {
        return UserForgetPasswordController.getUserEmail();
    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendOTP(String emailUser) {
        final String sendEmail = "lmslibrary8386@gmail.com";
        final String sendPassword = "g y d o h z e o o e g n d k r f";
        OTPcode = generateOTP();
        String subject = "Xac nhan email";
        String content = "Xin chao, chung toi den tu thu vien LMS\n" +
                "Sau day la ma OTP cua ban, xin dung tiet lo voi bat cu ai " + OTPcode + "\n";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendEmail, sendPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailUser));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email đã được gửi thành công!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private boolean verifyOTP(String number, String OTP) {
        if (number.equals(OTP)) {
            return true;
        } else {
            UserProfileUserFormController.showAlert("Thong bao", "Ban chua nhap dung OTP");
            return false;
        }
    }

    @FXML
    private void nextButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_CartoonAccent.wav");
        if (verifyOTP(otpcode.getText(), OTPcode)) {
            super.changeScene("forgetPassStage2.fxml", "Change password");
        }
    }

    @FXML
    private void clickEnter(KeyEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_CartoonAccent.wav");
        if (event.getCode() == KeyCode.ENTER) {
            nextButtonOnAction(null);
        }
    }
}

