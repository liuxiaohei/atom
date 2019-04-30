public interface DemoInterface {

    default boolean notEquals(DemoInterface e) {
        return !this.equals(e);
    }

    default String sunperString() {
        return "super " +  this.toString();
    }

    String demotest();

    default String anotherDemoTest() {
        return "another" + demotest();
    }

    static String staticDemo() {
        return "static";
    }

}
