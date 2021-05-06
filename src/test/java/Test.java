import com.glory.client.RpcProxy;
import com.glory.test.TestHello;
import com.glory.test.TestMinus;
import com.glory.test.TestPlus;

public class Test {
    public static void main(String[] args) {
        RpcProxy proxy = new RpcProxy("localhost", 9999);
        TestPlus testPlus = proxy.create(TestPlus.class);
        System.out.println(testPlus.sum(1, 2));
        TestMinus minus = proxy.create(TestMinus.class);
        System.out.println(minus.minus(100, 1));
        TestHello hello = proxy.create(TestHello.class);
        System.out.println(hello.hello("glory"));
    }
}
