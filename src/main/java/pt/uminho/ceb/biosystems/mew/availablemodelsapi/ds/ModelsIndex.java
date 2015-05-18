package pt.uminho.ceb.biosystems.mew.availablemodelsapi.ds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelsIndex implements Iterable<ModelInfo>{

	protected List<ModelInfo> modelInfoList;
	
	public ModelsIndex(int size) {
		modelInfoList = new ArrayList<ModelInfo>();
	}
	
	public void add(ModelInfo info) {
		modelInfoList.add(info);
	}

	public void print() {
		for(ModelInfo info : modelInfoList)
			info.print();
	}

	public ModelInfo get(int i) {
		return modelInfoList.get(i);
	}
	
	public int getSize(){
		return modelInfoList.size();
	}

	@Override
	public Iterator<ModelInfo> iterator() {
		return modelInfoList.iterator();
	}

}
