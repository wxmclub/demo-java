package com.wxmclub.demo.email;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

/**
 * commons-email使用示例，参考：http://commons.apache.org/proper/commons-email/userguide.html
 *
 * @author wxmclub@gmail.com
 * @version 1.0
 * @date 2017-07-26
 */
public class CommonsMailDemo {

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 初始化邮箱参数
     */
    private static void initEmailParam(Email email) throws EmailException {
        email.setHostName("smtp.163.com"); // 设置SMTP邮箱服务地址
        email.setAuthentication("test", "123456"); // 设置邮箱账号
        email.setCharset(DEFAULT_CHARSET); // 设置编码，防止正文乱码

        email.setFrom("test@163.com", "我自己", DEFAULT_CHARSET); // 设置发件人信息，与账号邮箱一致
    }

    /**
     * 设置收件人
     */
    private static void setToEmail(Email email) throws EmailException {
        email.addTo("test@163.com", "测试收件人", DEFAULT_CHARSET); // 收件人
        // email.addCc(""); // 抄送收件人
        // email.addBcc(""); // 密送收件人
    }

    /**
     * 发送纯文本邮件
     */
    public static void sendSimpleTextEmail() throws EmailException {
        SimpleEmail email = new SimpleEmail();
        initEmailParam(email);
        setToEmail(email);

        email.setSubject("commons-email纯文本邮件"); // 主题
        email.setMsg("这是邮件正文，纯文本字符串！"); // 邮件正文

        email.send(); // 发送邮件
    }

    /**
     * 发送带附件的邮件
     */
    public static void sendEmailsWithAttachments() throws EmailException, MalformedURLException, UnsupportedEncodingException {
        MultiPartEmail email = new MultiPartEmail();
        initEmailParam(email);
        setToEmail(email);

        email.setSubject("commons-email带附件纯文本邮件"); // 主题
        email.setMsg("这是邮件正文，纯文本字符串！"); // 邮件正文

        // 设置附件
        // EmailAttachment attachment = createLocalAttachment();
        EmailAttachment attachment = createUrlAttachment();
        email.attach(attachment);

        email.send(); // 发送邮件
    }

    /**
     * 使用本地资源创建附件
     */
    private static EmailAttachment createLocalAttachment() {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(CommonsMailDemo.class.getResource("/netease_logo.gif").getFile()); // 附件在本地的路径
        attachment.setDisposition(EmailAttachment.ATTACHMENT); // 附件类型
        attachment.setDescription("Picture of 163邮箱"); // 附件描述
        attachment.setName("163邮箱图片.gif"); // 附件名称
        // attachment.setName(MimeUtility.encodeText("163邮箱图片.gif")); // 附件名称，当出现乱码时使用
        return attachment;
    }

    /**
     * 使用在线资源创建附件
     */
    private static EmailAttachment createUrlAttachment() throws MalformedURLException, UnsupportedEncodingException {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setURL(new URL("https://mimg.127.net/logo/netease_logo.gif")); // 附件在本地的路径
        attachment.setDisposition(EmailAttachment.ATTACHMENT); // 附件类型
        attachment.setDescription("Picture of 163邮箱"); // 附件描述
        attachment.setName("163邮箱图片.gif"); // 附件名称
        // attachment.setName(MimeUtility.encodeText("163邮箱图片.gif")); // 附件名称，当出现乱码时使用
        return attachment;
    }

    /**
     * 发送内容为HTML格式的邮件，可添加附件，同MultiPartEmail
     */
    public static void sendHTMLFormattedEmail() throws EmailException, UnsupportedEncodingException {
        HtmlEmail email = new HtmlEmail();
        initEmailParam(email);
        setToEmail(email);

        email.setSubject("commons-email HTML邮件");

        // 创建图片
        String cid = email.embed("https://mimg.127.net/logo/netease_logo.gif", "163邮箱图片"); // 支持本地文件和URL在线文件
        String sb = "<h1>内容标题</h1>" +
                "<img src=\"https://mimg.127.net/logo/netease_logo.gif\"/>" +
                "<img src=\"cid:" + cid + "\"/>";
        email.setHtmlMsg(sb);
        email.setTextMsg("你的邮箱客户端不支持HTML消息");

        email.send();
    }

    /**
     * 发送内容为HTML格式的邮件（嵌入图片更方便），可添加附件，同MultiPartEmail
     */
    public static void sendImageHTMLFormattedEmail() throws EmailException, UnsupportedEncodingException, MalformedURLException {
        ImageHtmlEmail email = new ImageHtmlEmail();
        initEmailParam(email);
        setToEmail(email);

        email.setSubject("commons-email ImageHTML邮件");

        // 创建图片
        String cid = email.embed("https://mimg.127.net/logo/netease_logo.gif", "163邮箱图片"); // 支持本地文件和URL在线文件
        String sb = "<h1>内容标题</h1>" +
                "<img src=\"https://mimg.127.net/logo/netease_logo.gif\"/>" +
                "<img src=\"cid:" + cid + "\"/>";
        email.setDataSourceResolver(new DataSourceUrlResolver(new URL("https://mimg.127.net")));
        email.setHtmlMsg(sb);
        email.setTextMsg("你的邮箱客户端不支持HTML消息");

        email.send();
    }

    public static void main(String[] args) throws Exception {
        sendSimpleTextEmail();

        // sendEmailsWithAttachments();

        // sendHTMLFormattedEmail();

        // sendImageHTMLFormattedEmail();
    }

}
