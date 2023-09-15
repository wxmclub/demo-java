package com.wxmclub.demo.email;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * <pre>
 * javax.mail使用示例
 *
 * Mime中各对象关系图
 * +-----------------------multipart/mixed-----------------------+
 * |                                                             |
 * | +--------------multipart/related-----------------+          |
 * | |                                                |          |
 * | | +-----multipart/alternative-----+ +----------+ | +------+ |
 * | | |                               | | 内嵌资源 | | | 附件 | |
 * | | | +------------+ +------------+ | +----------+ | +---- -+ |
 * | | | | 纯文本正文 | | 超文本正文 | |              |          |
 * | | | +------------+ +------------+ | +----------+ | +------+ |
 * | | |                               | | 内嵌资源 | | | 附件 | |
 * | | +-------------------------------+ +----------+ | +------+ |
 * | |                                                |          |
 * | +------------------------------------------------+          |
 * |                                                             |
 * +-------------------------------------------------------------+
 * </pre>
 *
 * @author wxmclub@gmail.com
 * @version 1.0
 * @date 2017-07-26
 */
public class JMailDemo {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    private static Session session;

    /**
     * 初始化Session
     */
    public static void initSession() {
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", "smtp.163.com");    // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        session = Session.getDefaultInstance(props);
        session.setDebug(true); // debug模式，可以查看详细的日志
    }

    private static void sendMail(MimeMessage message) throws MessagingException, UnsupportedEncodingException {
        Transport transport = null;
        try {
            // 根据 Session 获取邮件传输对象
            transport = session.getTransport();
            transport.connect("test@163.com", "123456");

            // 设置发件人
            message.setFrom(new InternetAddress("test@163.com", "我自己", DEFAULT_CHARSET));

            // 设置收件人
            message.addRecipients(Message.RecipientType.TO, new Address[]{new InternetAddress("test@163.com", "测试收件人", DEFAULT_CHARSET)}); // 收件人
            // message.addRecipients(Message.RecipientType.CC, new Address[]{ new InternetAddress("wang021015@163.com", "测试收件人", DEFAULT_CHARSET) }); // 抄送收件人
            // message.addRecipients(Message.RecipientType.BCC, new Address[]{ new InternetAddress("wang021015@163.com", "测试收件人", DEFAULT_CHARSET) }); // 密送收件人

            // 设置发送时间
            message.setSentDate(new Date());

            // 保存设置
            message.saveChanges();

            // 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    /**
     * 发送简单文本邮件
     */
    public static void sendSimpleEmail() throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);

        // 邮件主题
        message.setSubject("javax.mail 简单文本邮件", DEFAULT_CHARSET);

        // 邮件正文，内容可以是纯文本，或者HTML内容
        message.setContent("这是邮件正文，纯文本字符串！", DEFAULT_CONTENT_TYPE);

        sendMail(message);
    }

    /**
     * 发送带带图片、附件邮件
     */
    public static void sendExtEmail() throws MessagingException, UnsupportedEncodingException, MalformedURLException {
        MimeMessage message = new MimeMessage(session);

        // 邮件主题
        message.setSubject("javax.mail 带图片及附件邮件", DEFAULT_CHARSET);

        // 邮件正文
        // 创建图片节点
        MimeBodyPart image1 = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource(JMailDemo.class.getResource("/netease_logo.gif").getFile())); // 读取本地文件
        // DataHandler dh = new DataHandler(new URLDataSource(new URL("https://mimg.127.net/logo/netease_logo.gif"))); // 读取网络文件
        image1.setDataHandler(dh);           // 将图片数据添加到“节点”
        image1.setContentID("netease_logo"); // 为“节点”设置一个唯一编号（在文本“节点”将引用该ID）

        // 创建文本节点
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        text.setContent("这是2张图片<br/><img src='cid:netease_logo1'/><img src='https://mimg.127.net/logo/netease_logo.gif'/>", DEFAULT_CONTENT_TYPE);

        // 把图片节点和文本节点，组成混合节点
        MimeMultipart mm_text_image = new MimeMultipart();
        mm_text_image.addBodyPart(text);
        mm_text_image.addBodyPart(image1);
        mm_text_image.setSubType("related"); // 关联关系

        // 将 文本+图片 的混合“节点”封装成一个普通“节点”
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        // 上面的 mm_text_image 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
        MimeBodyPart text_image = new MimeBodyPart();
        text_image.setContent(mm_text_image);

        // 创建附件“节点”
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler dh3 = new DataHandler(new FileDataSource(JMailDemo.class.getResource("/netease_logo.gif").getFile()));  // 读取本地文件
        attachment.setDataHandler(dh3);                                // 将附件数据添加到“节点”
        attachment.setFileName(MimeUtility.encodeText(dh3.getName())); // 设置附件的文件名（需要编码）

        // 把文本+图片混合封装的普通节点、附件节点组装成混合节点
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text_image);
        mm.addBodyPart(attachment); // 如果有多个附件，可以创建多个多次添加
        mm.setSubType("mixed");     // 混合关系

        message.setContent(mm_text_image);

        sendMail(message);
    }

    public static void main(String[] args) throws Exception {
        // 初始化邮件会话
        initSession();

        sendSimpleEmail();

        // sendExtEmail();
    }

}
