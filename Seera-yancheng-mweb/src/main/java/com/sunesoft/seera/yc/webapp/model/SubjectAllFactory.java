package com.sunesoft.seera.yc.webapp.model;

import com.sunesoft.seera.yc.core.lottery.application.SubjectService;

import com.sunesoft.seera.yc.core.lottery.application.dtos.SubjectDto;
import org.apache.velocity.tools.generic.ClassTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by temp on 2016/9/6.
 */
@Component
public class SubjectAllFactory {

  /*  @Autowired
    private SubjectService subjectService;
    private static Map<Integer,Light> light=new HashMap<>();
    private static  List<SubjectDto> mapSub0 = new ArrayList<>();
    private static  List<SubjectDto> mapSub1 = new ArrayList<>();

    public Light getLight(Integer number) {
        if (light.size() == 0) {
            getAllLight();
        }
        return light.get(number);
    }

    public void getAllLight() {
        if(mapSub0==null){
            mapSub0=new ArrayList<>();
        }
        if(mapSub0.size()==0){
            mapSub0=subjectService.getByType(0);
        }
        for(int i=0;i<mapSub0.size();i++){
            List<SubjectDto> list=getMapSub1();
            Light temp=new Light();
            temp.setList(list);
            temp.setSubjectDto(mapSub0.get(i));
            light.put(i,temp);
        }
    }

    public List<SubjectDto> getMapSub1(){
        List<SubjectDto> list=new ArrayList<>();
        if(mapSub1.size()==0){
            mapSub1=subjectService.getByType(1);
        }
        List lt=new ArrayList<>();
        for(int i=0;i<4;i++) {
            int temp=-1;
            do{
               temp = (int)Math.floor(Math.random()*10);
            }while (temp>=mapSub1.size() ||  lt.contains(temp));
            lt.add(temp);
            list.add(mapSub1.get(temp));
        }
        return list;
    }

    public void clear(){
        light=new HashMap<>();
        mapSub0 = new ArrayList<>();
        mapSub1 = new ArrayList<>();

    }*/

    @Autowired
    private SubjectService subjectService;
    private static Map<Integer,Light> light=new HashMap<>();
    private static  List<SubjectDto> mapSub0 = new ArrayList<>();
    private static  List<SubjectDto> mapSub1 = new ArrayList<>();

    public Light getLight(Integer number) {
        if (light.size() == 0) {
            getAllLight();
        }
        return light.get(number-1);
    }

    public void getAllLight() {
        if(mapSub0==null){
            mapSub0=new ArrayList<>();
        }
        if(mapSub0.size()==0){
            mapSub0=subjectService.getByType(0);
        }
        for(int i=0;i<mapSub0.size();i++){
        //    List<SubjectDto> list=getMapSub1(i);
            Light temp=new Light();
        //    temp.setList(list);
            temp.setSubjectDto(mapSub0.get(i));
            light.put(i,temp);
        }
    }

    public List<SubjectDto> getMapSub1(int k){
        List<SubjectDto> list=new ArrayList<>();
        if(mapSub1.size()==0){
            mapSub1=subjectService.getByType(1);
        }
        List lt=new ArrayList<>();
        for(int i=0;i<4;i++) {
            int temp=-1;
            do{
                temp = (int)Math.floor(Math.random()*100);
            }while (temp>=mapSub1.size() || temp==(k-1) ||  lt.contains(temp));
            lt.add(temp);
            list.add(mapSub1.get(temp));
        }
        return list;
    }

    public void clear(){
        light=new HashMap<>();
        mapSub0 = new ArrayList<>();
        mapSub1 = new ArrayList<>();

    }


}
