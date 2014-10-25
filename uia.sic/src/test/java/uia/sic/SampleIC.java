package uia.sic;

import java.util.Set;
import java.util.TreeSet;

public class SampleIC extends SimpleIC {

    @Override
    public void valueChanged(WritableTag tag) {
        System.out.println(tag);
    }

    @Override
    protected Set<String> getInputTags() {
        TreeSet<String> result = new TreeSet<String>();
        result.add("//s200/PID/M4/0001/$online");
        return result;
    }

    @Override
    protected Set<String> getOutputTags() {
        TreeSet<String> result = new TreeSet<String>();
        return result;
    }

}
