package matchers

import tw.xcc.gumtree.helper.gumTree

private val realTree1L =
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

private val realTree1R =
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

internal val realTree1 = realTree1L to realTree1R