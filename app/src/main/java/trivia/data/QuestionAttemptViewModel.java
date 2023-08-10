package trivia.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class QuestionAttemptViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Question>> questions = new MutableLiveData< >();
    public MutableLiveData<ArrayList<String>> correctAnswers = new MutableLiveData< >();
}
