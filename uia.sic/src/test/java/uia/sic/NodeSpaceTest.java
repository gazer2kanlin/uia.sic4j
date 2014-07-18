package uia.sic;

import java.util.List;

import org.junit.Test;

public class NodeSpaceTest {

    @Test
    public void testBrowseAll() {
        NodeSpace space = new NodeSpace(new SampleTagLoader());

        List<WritableTag> tags = space.browseAll();
        for (WritableTag tag : tags) {
            System.out.println(tag);
        }
    }

    @Test
    public void testBrowsePath() {
        System.out.println("browse //s200/PID/*");
        NodeSpace space = new NodeSpace(new SampleTagLoader());
        List<WritableTag> tags1 = space.browse("//s200/PID");
        for (WritableTag tag : tags1) {
            System.out.println(tag);
        }

        System.out.println("browse //s200/PID/M4MINI");
        List<WritableTag> tags2 = space.browse("s200/PID/M4MINI");
        for (WritableTag tag : tags2) {
            System.out.println(tag);
        }
    }

    @Test
    public void testBrowsePathAndName() {
        NodeSpace space = new NodeSpace(new SampleTagLoader());
        List<WritableTag> tags1 = space.browse("//s200", "online");
        for (WritableTag tag : tags1) {
            System.out.println(tag);
        }

        List<WritableTag> tags2 = space.browse("//", "power");
        for (WritableTag tag : tags2) {
            System.out.println(tag);
        }
    }

    @Test
    public void testValueChanged() {
        NodeSpace space = new NodeSpace(new SampleTagLoader());
        List<WritableTag> tags1 = space.browse("//s200", "online");
        for (WritableTag tag : tags1) {
            tag.addEventListener(new TagEventListener() {

                @Override
                public void valueChanged(WritableTag tag) {
                    System.out.println(tag);
                }

            });
        }

        List<WritableTag> tags2 = space.browse("//s200", "online");
        for (WritableTag tag : tags2) {
            tag.writeValue(false);
        }
    }
}
