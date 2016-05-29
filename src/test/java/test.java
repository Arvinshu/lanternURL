/**
 * Created by Arvin on 2016/5/25.
 */
//主动回收测试
public class test {
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("我要挂了");
    }

    public static void main(String[] args) {
        test t = new test();
        t = null;
        //主动回收，析构函数
        System.gc();
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
