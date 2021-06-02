public class Test {

    public static void main(String[] args) {
        new Test().p();
    }

    private int value;

    public void p() {
        System.out.println(this.value);
    }
}
