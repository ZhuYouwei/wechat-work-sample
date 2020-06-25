package com.example.wechatwork.Job;


import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledDisclaimerBroadcastJob {

    String DISCLAIMER_MSG = "J.P. Morgan Funds believes an informed advisor community means a better off investment community.\n" +
            "\n" +
            "As such, “let’s make every investor better off” and the communications associated with this marketing line are meant to showcase that J.P. Morgan Funds would like to partner with Financial Advisors to help them help their clients.\n" +
            "\n" +
            "This in no way is meant to be promissory regarding performance of products.\n" +
            "\n" +
            "J.P. Morgan Funds and J.P. Morgan ETFs are distributed by JPMorgan Distribution Services, Inc., which is an affiliate of JPMorgan Chase & Co. Affiliates of JPMorgan Chase & Co. receive fees for providing various services to the funds. JPMorgan Distribution Services, Inc. is a member of FINRA.  FINRA's BrokerCheck\n" +
            "\n" +
            "Investors should carefully consider the investment objectives and risks as well as charges and expenses of the JPMorgan ETF before investing. The summary and full prospectuses contain this and other information about the ETF. Read the prospectus carefully before investing. Call 1-844-4JPMETF or visit www.jpmorganETFs.com to obtain a prospectus.\n" +
            "\n" +
            "Contact JPMorgan Distribution Services, Inc. at 1-800-480-4111 for a fund prospectus. You can also visit us at www.jpmorganfunds.com. Investors should carefully consider the investment objectives and risks as well as charges and expenses of the fund before investing. The prospectus contains this and other information about the fund. Read the prospectus carefully before investing. Opinions and statements of financial market trends that are based on current market conditions constitute our judgment and are subject to change without notice. We believe the information provided here is reliable but should not be assumed to be accurate or complete. The views and strategies described may not be suitable for all investors. J.P. Morgan Funds are distributed by JPMorgan Distribution Services, Inc., member FINRA. J.P. Morgan Asset Management is the marketing name for the asset management businesses of JPMorgan Chase & Co. and its affiliates worldwide.\n" +
            "\n" +
            "Comments and opinions posted by users are the responsibility of the person who posted them. Any Twitter mentions and followers do not constitute endorsements of any kind. Due to privacy and security policies, we cannot use Twitter to communicate directly with users. Therefore, please do not use Twitter and direct messaging to submit questions or request transactions. If you have questions, please call J.P. Morgan Funds at 1-800-480-4111.";

    @Scheduled(cron = "0 0 0 ? * * *")
    public void broadcastDisclaimer() {
        // TODO: broadcast the msg here
    }
}
