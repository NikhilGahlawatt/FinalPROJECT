package trivia.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ResponseViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Response>> responsesData = new MutableLiveData< >();
}