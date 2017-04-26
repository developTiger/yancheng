package com.sunesoft.seera.yc.webapp.model;

import com.sunesoft.seera.yc.core.picture.application.IPictureService;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by temp on 2016/9/6.
 */
@Component
public class PictureAllFactory {

    @Autowired
    private IPictureService iPictureService;
    private static Map<String, Map<Integer, List<PictureDto>>> mapPics = new HashMap<>();

    public Map<Integer, List<PictureDto>> getPicByLocation(String location){
        if(mapPics.size()==0){
            getAllPic();
        }
        return mapPics.get(location);
    }


    public  PictureDto getImage(String location,Integer position){

        Map<Integer, List<PictureDto>> map = mapPics.get(location);
       if( map!=null ){

           if(map.get(position)!=null)

           return map.get(position).get(0);
       }
        return null;
    }

//    public PictureDto getImageList(String location,String position){
//
//    }

    public void resetPicMap(){
        mapPics = new HashMap<>();
        getAllPic();
    }

    private void  getAllPic() {
        List<PictureDto> list = iPictureService.getAll();
        for (PictureDto pic : list) {
            Map<Integer, List<PictureDto>> currentLoactionPics = mapPics.get(pic.getLocation());
            if (currentLoactionPics != null) {
                List<PictureDto> piclist = currentLoactionPics.get(pic.getPosition());
                if (piclist != null) {
                    piclist.add(pic);
                } else {
                    piclist = new ArrayList<>();
                    piclist.add(pic);
                    currentLoactionPics.put(pic.getPosition(),piclist);
                    //currentLoactionPics= tempCurrentLocationPics(currentLoactionPics,pic.getPosition(),piclist);
                }
            } else {
                currentLoactionPics = new HashMap<>();
                List<PictureDto> piclist = new ArrayList<>();
                piclist.add(pic);
                currentLoactionPics.put(pic.getPosition(),piclist);
              //  currentLoactionPics= tempCurrentLocationPics(currentLoactionPics,pic.getPosition(),piclist);
            }
            mapPics.put(pic.getLocation(), currentLoactionPics);
        }
    }

    private Map<Integer, List<PictureDto>> tempCurrentLocationPics(Map<Integer, List<PictureDto>> currentLoactionPics, Integer position, List<PictureDto> piclist) {

        if(currentLoactionPics.size()==0){
            currentLoactionPics.put(position,piclist);
            return currentLoactionPics;
        }
        boolean flag=true;
        for(Integer key:currentLoactionPics.keySet()){
            if(key!=position){
                flag=false;
            }else{
                currentLoactionPics.get(key).add(piclist.get(0));
                flag=true;
            }
        }
        if(!flag){
            currentLoactionPics.put(position,piclist);
        }
        return currentLoactionPics;
    }

  /*  private void getAllPics(){
        List<PictureDto> list=iPictureService.getAll();
        Map<Integer, List<PictureDto>> currentTemp = new HashMap<>();
        List<PictureDto> piclists=new ArrayList<>();
        for(PictureDto pic : list){
            if(pic.getPosition()!=null) {
                switch (pic.getPosition()) {
                    case 1:
                        piclists.add(pic);
                    case 2:
                        piclists.add(pic);
                    case 3:
                        piclists.add(pic);
                    case 4:
                        piclists.add(pic);
                    case 5:
                        piclists.add(pic);
                }
            }
        }
        for()

    }*/

/*    private void  getAllPic() {
        List<PictureDto> list = iPictureService.getAll();
        Map<Integer, List<PictureDto>> currentTemp = new HashMap<>();
        for (PictureDto pic : list) {
            Map<Integer, List<PictureDto>> currentLoactionPics = mapPics.get(pic.getLocation());
            List<PictureDto> piclists = new ArrayList<>();
            if (currentLoactionPics != null) {
                List<PictureDto> piclist = currentLoactionPics.get(pic.getPosition());
                if (piclist != null) {
                    piclist.add(pic);
                } else {
                    piclist = new ArrayList<>();
                    piclist.add(pic);
                    currentLoactionPics.put(pic.getPosition(), piclist);
                }
            } else {
                currentLoactionPics = new HashMap<>();
                if (pic.getPosition() != null) {
                    switch (pic.getPosition()) {
                        case 1:
                            piclists.add(pic);
                        case 2:
                            piclists.add(pic);
                        case 3:
                            piclists.add(pic);
                        case 4:
                            piclists.add(pic);
                        case 5:
                            piclists.add(pic);
                    }
                }
            }
        }
    }*/
}
