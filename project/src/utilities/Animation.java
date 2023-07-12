package utilities;

public interface Animation {

	void onStart();

	void onAnimate(double percent);

	void onStop();

	void onEnd();

}