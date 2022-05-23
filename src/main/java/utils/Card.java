package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Card {
    private String number;
    private String year;
    private String month;
    private String name;
    private String cvc;
}
