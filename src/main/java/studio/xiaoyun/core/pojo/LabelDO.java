package studio.xiaoyun.core.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 标签
 */
@Entity
@Table(name="xy_label")
public class LabelDO {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "studio.xiaoyun.core.pojo.UuidIdentifierGenerator")
    @Column(length = 32, name = "labelid")
    private String labelId;

    @Column(nullable = false,length = 20)
    private String text;

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
