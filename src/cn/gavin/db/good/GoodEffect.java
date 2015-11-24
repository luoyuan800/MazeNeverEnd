package cn.gavin.db.good;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/24/2015.
 */
public enum GoodEffect {
;
    private Script script;
    private GoodEffect(Script script){
        this.script = script;
    }
    public void useGood(){
        script.use();
    }
}
