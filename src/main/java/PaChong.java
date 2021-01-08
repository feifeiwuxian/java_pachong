import com.google.common.io.Resources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class PaChong {
    public static void main(String[] args) throws Exception {
        //判断当前是windows还是mac
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        if (osName.startsWith("Mac OS")) {
            // 苹果
            System.setProperty("webdriver.chrome.driver", "./lib/chromedriver" );
        } else if (osName.startsWith("Windows")) {
            // windows
            System.setProperty("webdriver.chrome.driver", "./lib/chromedriver.exe" );
        } else {
            // unix or linux
        }


        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        System.out.println("程序正在启动....");
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        System.out.println("启动成功！正在查询中....");
        String url = "https://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=detail&fr=&hs=0&xthttps=111110&sf=1&fmq=1609316316073_R&pv=&ic=0&nc=1&z=&se=&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word=%E7%BE%8E%E5%A5%B3&oq=%E7%BE%8E%E5%A5%B3&rsp=-1";
        driver.get(url);
        // 休眠1s,为了让js执行完
//        Thread.sleep(4000l);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String source = driver.getPageSource();
//        System.out.println(source);

        Document document = Jsoup.parse(source);
        // 2.在网页中查找图片（很多图片）
        Elements elements = document.select(".imgitem img.main_img");

        System.out.println("已经为您找到" + elements.size() + "个美女");
        getImage(elements);
        System.out.println("已经成功将美女放在 imaegs 文件夹中，请打开观赏");
        driver.close();

    }
    public static void getImage(Elements elements) throws Exception {
        // 文件路径
        String filePath = System.getProperty("user.dir") + File.separator + "images" + File.separator;
        // 创建目录
        creatDir(filePath);
        // 3.迭代（循环）一个一个处理
        for(int i= 0 ; i<elements.size() ; i++){
            String s= elements.get(i).attr("data-imgurl");
            System.out.println("开始下载第" + (i+1)  +"个美女");
            System.out.println(s);
            // 根据图片的地址（是不完整的），拼凑一个完整的图片地址
            URL imgUrl = new URL(s);
            //  先和图片保持有效的连接
            URLConnection connection = imgUrl.openConnection();
            // 为每一张图片 创建输入流
            InputStream ru = connection.getInputStream();

            // 为每一张图片 创建输出流
            FileOutputStream chu = new FileOutputStream(new File(filePath + i +".jpg"));

            // 4.拿一个图片，就下载一个图片
            // 每读取一次，将读取的字节数据放在里面（ 每次运输数据的背包）
            byte bs[] = new byte[1024];
            // 每次读取的字节数
            int count = 0 ;
            while( (count= ru.read(bs) ) != -1 ){
                chu.write(bs, 0, count);
            }

            // 释放资源，避免浪费，提高性能
            chu.close();
            ru.close();
        }
    }

    private static void creatDir(String filePath) {
        File dir = new File(filePath);
        if (dir.exists()) {
            // 删除
            deleteDir(dir);
        } else {
            dir.mkdir();
        }
    }

    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            for (String fileStr : dir.list()) {
                String filePath = dir.getPath() + File.separator + fileStr;
                deleteDir(new File(filePath));
            }
        } else {
            dir.delete();
            System.out.println("删除文件" + dir.getAbsolutePath());
        }
    }
}


