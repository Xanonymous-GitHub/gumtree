package shared

import helpers.gumTree
import tw.xcc.gumtree.model.GumTree

/**
 * The real-world tree 1.
 * (Mentioned in GumTree paper Figure 1)
 * It is equivalent to the following Java code:
 * ```java
 * public class Test {
 *   public String foo(int i) {
 *     if (i == 0) return "Foo!";
 *   }
 * }
 * ```
 * */
private val realTree1L: GumTree
    get() =
        gumTree("CompilationUnit") {
            child("TypeDeclaration") {
                child("Modifier", "public")
                child("SimpleName", "Test")
                child("MethodDeclaration") {
                    child("Modifier", "public")
                    child("SimpleType", "String") {
                        child("SimpleName", "String")
                    }
                    child("SimpleName", "foo")
                    child("SingleVariableDeclaration") {
                        child("PrimitiveType", "int")
                        child("SimpleName", "i")
                    }
                    child("Block") {
                        child("IfStatement") {
                            child("InfixExpression", "==") {
                                child("SimpleName", "i")
                                child("NumberLiteral", "0")
                            }
                            child("ReturnStatement") {
                                child("StringLiteral", "Foo!")
                            }
                        }
                    }
                }
            }
        }

/**
 * The real-world tree 1.
 * (Mentioned in GumTree paper Figure 1)
 * It is equivalent to the following Java code:
 * ```java
 * public class Test {
 *   public String foo(int i) {
 *     if (i == 0) return "Bar";
 *     else if (i == -1) return "Foo!";
 *   }
 * }
 * ```
 */
private val realTree1R: GumTree
    get() =
        gumTree("CompilationUnit") {
            child("TypeDeclaration") {
                child("Modifier", "public")
                child("SimpleName", "Test")
                child("MethodDeclaration") {
                    child("Modifier", "public")
                    child("SimpleType", "String") {
                        child("SimpleName", "String")
                    }
                    child("SimpleName", "foo")
                    child("SingleVariableDeclaration") {
                        child("PrimitiveType", "int")
                        child("SimpleName", "i")
                    }
                    child("Block") {
                        child("IfStatement") {
                            child("InfixExpression", "==") {
                                child("SimpleName", "i")
                                child("NumberLiteral", "0")
                            }
                            child("ReturnStatement") {
                                child("StringLiteral", "Bar")
                            }
                            child("IfStatement") {
                                child("InfixExpression", "==") {
                                    child("SimpleName", "i")
                                    child("PrefixExpression", "-") {
                                        child("NumberLiteral", "1")
                                    }
                                }
                                child("ReturnStatement") {
                                    child("StringLiteral", "Foo!")
                                }
                            }
                        }
                    }
                }
            }
        }

/**
 * @see realTree1L
 * @see realTree1R
 * */
internal val realTree1: Pair<GumTree, GumTree>
    get() = realTree1L to realTree1R