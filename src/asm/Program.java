package asm;

import asm.section.Section;
import asm.section.Text;

import java.util.ArrayList;

/**
 * @author F
 * 整个Mx项目
 * asm中由section组成
 */
public class Program {
    //所有的函数都被放在一个代码段
    public Text text = new Text();

    //全局的变量、字符串常量
    public ArrayList<Section> globalDefs = new ArrayList<>();

}
