package carrental.tables;

import javax.swing.table.DefaultTableModel;

import carrental.models.Car;
import carrental.models.CarInventory;

public class CarTableModel extends DefaultTableModel {

    public CarTableModel(CarInventory inventory) {
        super(createData(inventory), createColumnNames());
    }

    private static Object[][] createData(CarInventory inventory) {
        Object[][] data = new Object[inventory.getCarList().size()][createColumnNames().length];

        for (int i = 0; i < inventory.getCarList().size(); i++) {
            Car car = inventory.getCarList().get(i);
            data[i] = car.getCarData();
        }

        return data;
    }

    private static String[] createColumnNames() {
        return new String[]{"Manufacturer", "Model", "Registration Info", "Color", "Year of Production", "Price/day", "Comfort Level", "Additional Features"};
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Make all cells uneditable
    }

    public void addCarRecord(Car newCar) {
        // Add a new row with the data of the new car
        Object[] rowData = newCar.getCarData();
        addRow(rowData);
    }
}
