package Main;

import java.util.Date;

public class Issues {
    private long id;
    private String description;
    private String priority;
    private Date date;
    private String status;
    private long empoyeeId;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmpoyeeId(long empoyeeId) {
        this.empoyeeId = empoyeeId;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public long getEmpoyeeId() {
        return empoyeeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }
}
