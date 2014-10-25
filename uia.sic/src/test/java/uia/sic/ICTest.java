package uia.sic;


public class ICTest {

    public void test() {

        NodeSpace space = new NodeSpace(new SampleTagLoader());
        SampleIC ic = new SampleIC();
        ic.bind(space);
        ic.start();

        space.single("//s200/PID/M4/0001/", "online").writeValue(false);

    }
}
