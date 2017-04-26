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
                }
            } else {
                currentLoactionPics = new HashMap<>();
                List<PictureDto> piclist = new ArrayList<>();
                piclist.add(pic);
                currentLoactionPics.put(pic.getPosition(),piclist);
            }
            mapPics.put(pic.getLocation(), currentLoactionPics);
        }
    }

}
