package com.theironyard;

import javax.persistence.*;

/**
 * Created by macbookair on 11/11/15.
 */
@Entity
public class Purchase {
    @Id
    @GeneratedValue
    Integer id;

    String date;
    String credit_card;
    int cvv;
    String category;

    @ManyToOne
    Customer customer;

}
