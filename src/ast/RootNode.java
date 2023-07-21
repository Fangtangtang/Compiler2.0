package ast;

import utility.Position;
import java.util.*;

/**
 * @author F
 * AST的根节点，代表g4文件中的program
 * ------------------------------------------------
 * program: declaration* EOF;
 * declaration:
 *     functionDeclaration         //函数声明定义
 *     | declarationStatement      //变量、常量、类
 *     ;
 */
public class RootNode extends ASTNode{

}
