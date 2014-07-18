package uia.sic.rmis;

import java.util.ArrayList;
import java.util.List;

import uia.sic.BooleanTag;
import uia.sic.NumberTag;
import uia.sic.StringTag;
import uia.sic.TagLoader;
import uia.sic.WritableTag;

public class SampleTagLoader implements TagLoader {

	@Override
	public List<WritableTag> load() {
		ArrayList<WritableTag> tags = new ArrayList<WritableTag>();

		tags.add(new BooleanTag("//s200/PID/M4/0001/", "online", true, false));
		tags.add(new StringTag("//s200/PID/M4/0001/", "ups", "2", false));
		tags.add(new BooleanTag("//s200/PID/P1/0003/", "online", true, false));
		tags.add(new StringTag("//s200/PID/P1/0003/", "ups", "8", false));
		tags.add(new StringTag("//s200/PID/P1/0003/", "power", "9", false));
		tags.add(new NumberTag("//s200/PID/P1/0003/", "status", 11, false));
		tags.add(new BooleanTag("//s200/PID/M4MINI/0002/", "online", true,
				false));
		tags.add(new StringTag("//s200/PID/M4MINI/0002/", "ups", "4", false));
		tags.add(new StringTag("//s200/PID/M4MINI/0002/", "power", "5", false));
		tags.add(new NumberTag("//s200/PID/M4MINI/0002/", "status", 6, false));

		tags.add(new BooleanTag("//s300/PID/TTN/0003/", "online", true, false));
		tags.add(new StringTag("//s300/PID/TTN/0003/", "ups", "6", false));
		tags.add(new BooleanTag("//s300/PID/VDU/0004/", "online", true, false));
		tags.add(new StringTag("//s300/PID/VDU/0004/", "ups", "8", false));
		tags.add(new StringTag("//s300/PID/VDU/0004/", "power", "9", false));
		tags.add(new NumberTag("//s300/PID/VDU/0004/", "status", 10, false));
		tags.add(new BooleanTag("//s300/PID/E1/0001/", "online", true, false));
		tags.add(new StringTag("//s300/PID/E1/0001/", "ups", "2", false));
		tags.add(new StringTag("//s300/PID/E1/0001/", "power", "3", false));
		tags.add(new BooleanTag("//s300/PID/E2/0002/", "online", true, false));

		return tags;
	}
}
