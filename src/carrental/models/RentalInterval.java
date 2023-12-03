package carrental.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class RentalInterval implements Serializable {
    private UUID rentId;
    private Date startDate;
    private Date endDate;
    private static final long serialVersionUID = 8292437162251410900L;

    public RentalInterval(UUID rentId, Date startDate, Date endDate) {
        this.rentId = rentId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getRentId() {
        return rentId;
    }

    public void setRentId(UUID rentId) {
        this.rentId = rentId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
