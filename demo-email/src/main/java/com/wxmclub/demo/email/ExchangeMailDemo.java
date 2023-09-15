package com.wxmclub.demo.email;

import java.net.URI;
import java.net.URISyntaxException;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

/**
 * exchange使用示例
 *
 * @author wxmclub@gmail.com
 * @version 1.0
 * @date 2017-07-26
 */
public class ExchangeMailDemo {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static ExchangeService service;

    private static void initExchangeService() throws URISyntaxException {
        service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        service.setUrl(new URI("https://domain.com/ews/exchange.asmx")); // 服务地址一般是http://域名/ews/exchange.asmx格式

        //用户认证信息
        ExchangeCredentials credentials = new WebCredentials("test", "123456");
        service.setCredentials(credentials);
    }

    /**
     * 设置收件人
     */
    private static void setToEmail(EmailMessage email) throws ServiceLocalException {
        email.getToRecipients().add("测试收件人", "test@163.com");
        // email.getCcRecipients().add("", ""); // 抄送收件人
        // email.getBccRecipients().add("", ""); // 密送收件人
    }

    /**
     * 发送纯文本邮件
     */
    public static void sendSimpleTextEmail() throws Exception {
        EmailMessage email = new EmailMessage(service);
        setToEmail(email);

        // 邮件主题
        email.setSubject("exchange 简单文本邮件");

        // 邮件正文
        MessageBody body = MessageBody.getMessageBodyFromText("这是邮件正文，纯文本字符串！");
        body.setBodyType(BodyType.Text);
        email.setBody(body);

        email.send(); // 发送邮件
    }

    /**
     * 发送HTML文本邮件
     */
    public static void sendHtmlTextEmail() throws Exception {
        EmailMessage email = new EmailMessage(service);
        setToEmail(email);

        // 邮件主题
        email.setSubject("exchange HTML文本邮件");

        // 邮件正文
        String html = "<h1>内容标题</h1>" +
                "<img src=\"https://mimg.127.net/logo/netease_logo.gif\"/>";
        MessageBody body = MessageBody.getMessageBodyFromText(html);
        body.setBodyType(BodyType.HTML);
        email.setBody(body);

        email.send(); // 发送邮件
    }

    /**
     * 发送带附件邮件
     */
    public static void sendAttachmentsEmail() throws Exception {
        EmailMessage email = new EmailMessage(service);
        setToEmail(email);

        // 邮件主题
        email.setSubject("exchange 带附件邮件");

        // 邮件正文
        MessageBody body = MessageBody.getMessageBodyFromText("这是邮件正文，纯文本字符串！");
        body.setBodyType(BodyType.Text);
        email.setBody(body);

        // 邮件附件，可添加多个
        email.getAttachments().addFileAttachment("163邮箱图片.gif", ExchangeMailDemo.class.getResource("/netease_logo.gif").getFile());

        email.send(); // 发送邮件
    }

    public static void main(String[] args) throws Exception {
        // 初始化服务
        initExchangeService();

        sendSimpleTextEmail();

        // sendHtmlTextEmail();

        // sendAttachmentsEmail();
    }

}
