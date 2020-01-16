package sqs.pageobjects;


import org.apache.log4j.Logger;
import sqs.framework.FrameworkData;
import sqs.pageobjects.pages.web.*;

public class PageFactory  {
    public static final CyclosLoginPage cyclosLoginPage  = new CyclosLoginPage();
    public static final CyclosHomePage cyclosHomePage  = new CyclosHomePage();
    public static final CyclosBankingPage cyclosBankingPage = new CyclosBankingPage();
    public static final CyclosPaymentToUserPage cyclosPaymentToUserPage = new CyclosPaymentToUserPage();
    public static final CyclosPaymentReviewPage cyclosPaymentReviewPage  = new CyclosPaymentReviewPage();

    private static Logger logger=Logger.getLogger(FrameworkData.class);



    public void init(){
        logger.info("Page Objects initialized");
    }


}