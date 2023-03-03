package org.jff.Entity;

import lombok.Data;

@Data
public class LikeableObject {

    private Integer likeCount;
    private Integer dislikeCount;

    public void updateLikeStatus(int oldStatus,int newStatus){
        if(oldStatus==0){
            if(newStatus==1){
                likeCount++;
            }else if(newStatus==-1){
                dislikeCount++;
            }
        }
        if(oldStatus==1){
            if(newStatus==0){
                likeCount--;
            }else if(newStatus==-1){
                likeCount--;
                dislikeCount++;
            }
        }
        if(oldStatus==-1){
            if(newStatus==0){
                dislikeCount--;
            }else if(newStatus==1){
                dislikeCount--;
                likeCount++;
            }
        }
    }
}
