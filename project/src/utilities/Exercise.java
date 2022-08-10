package utilities;

public record Exercise(int id, String name, int caloriesPerMin) {

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getCaloriesPerMin() {
		return caloriesPerMin;
	}

}