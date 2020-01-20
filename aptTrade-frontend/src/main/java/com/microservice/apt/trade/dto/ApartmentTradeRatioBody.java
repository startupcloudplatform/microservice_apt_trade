package com.microservice.apt.trade.dto;

import com.microservice.apt.olv.dto.OlvResponse;
import lombok.*;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApartmentTradeRatioBody {

    @NonNull
    private ApartmentTradeResponse aptTrade;
    @NonNull
    private List<OlvResponse> declaredValueResponses;
    private long avgDeclaredValue; //평균 공시가격
    private String ratio; //비율

    public void setAvgDeclaredValue() {
        long totalPrice = 0;
        //((전용면적 * 단위 면적당 가격) * n )/n
        for( OlvResponse value: declaredValueResponses ){
            totalPrice += aptTrade.getArea() * value.getUnitArValue();
        }
        String avgDeclaredValueStr = String.valueOf(totalPrice/declaredValueResponses.size());
        String avgDeclaredValueSplit ="";
        if(!StringUtils.isEmpty(avgDeclaredValueStr) && !avgDeclaredValueStr.equals("0")){
            avgDeclaredValueSplit = avgDeclaredValueStr.substring(0, avgDeclaredValueStr.length()-4);
        }
        avgDeclaredValueSplit = StringUtils.isEmpty(avgDeclaredValueSplit) ? "0" :avgDeclaredValueSplit;
        this.avgDeclaredValue = Long.parseLong(avgDeclaredValueSplit);
    }

    public void setRatio() {
        String[] splits = aptTrade.getTransactionAmount().split(",");
        StringBuilder transactionAmount = new StringBuilder();
        for(String s : splits ){
            transactionAmount.append(s);
        }
        double rate = ( (double)avgDeclaredValue / (double)(Long.parseLong(transactionAmount.toString())) )* 100;
        String disPattern = "0.#";
        DecimalFormat form = new DecimalFormat(disPattern);

        this.ratio =form.format(rate) +"%";
    }
}
