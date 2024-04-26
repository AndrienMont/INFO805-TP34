package fr.usmb.m1isc.compilation.tp;

public class Noeud {
    private static int idWhile = 0;
    private static int idIf = 0;

    private int id;

    public TypeNode type;
    public Object value;
    public Noeud fil1;
    public Noeud fil2;

    public Noeud() {
        type = TypeNode.NONE;
        value = null;
        fil1 = null;
        fil2 = null;
    }

    public Noeud(TypeNode type) {
        this.type = type;
        value = type;
        fil1 = null;
        fil2 = null;
    }

    public Noeud(Object value) {
        type = TypeNode.NONE;
        this.value = value;
        fil1 = new Noeud();
        fil2 = new Noeud();
    }

    public Noeud(Object value, Noeud fils, Noeud fils2) {
        type = TypeNode.NONE;
        this.value = value;
        this.fil1 = fils;
        this.fil2 = fils2;
    }

    public Noeud(TypeNode type, Object value) {
        this.type = type;
        this.value = value;
        fil1 = new Noeud();
        fil2 = new Noeud();
        switch (type) {
            case WHILE -> id = idWhile++;
            case COMPARATOR -> id = idIf++;
        }
    }

    public Noeud(TypeNode type, Noeud fils, Noeud fils2) {
        this.type = type;
        this.value = type;
        this.fil1 = fils;
        this.fil2 = fils2;
    }

    public Noeud(TypeNode type, Object value, Noeud fils, Noeud fils2) {
        this.type = type;
        this.value = value;
        this.fil1 = fils;
        this.fil2 = fils2;
    }

    public void print() {
        toArbre("");
    }

    @Override
    public String toString() {
        switch (this.type) {
            case PV :
                return this.fil1.toString() + this.fil2.toString();
            case LET:
                return this.fil2.toString() +
                        "\tmov " + this.fil1.value + ", eax\n";
            case VAR:
                return "\tmov eax, " + this.value + "\n";
            case VALUE:
                return "\tmov eax, " + this.value + "\n";
            case MOINS_UNAIRE:
                return "\tmov eax, " + this.value + "\n" +
                        "\tpush eax\n" +
                        "\tmov eax, 0\n" +
                        "\tpop ebx\n" +
                        "\tsub eax, ebx\n";
            case OUTPUT:
                return this.fil1.toString() +
                        "\tout eax\n";
            case INPUT:
                return "\tin eax\n";
            case OPERATOR:
                String op = "";
                switch (this.value.toString()) {
                    case "*" :
                        op = "\tmul eax, ebx\n";
                        break;
                    case "/" :
                        op = "\tdiv ebx, eax\n" +
                                "\tmov eax, ebx\n";
                        break;
                    case "+" :
                        op = "\tadd eax, ebx\n";
                        break;
                    case "-" :
                        op = "\tsub ebx, eax\n" +
                                "\tmov eax, ebx\n";
                        break;
                    case "%" :
                        op = "\tmov ecx,ebx\n" +
                                "\tdiv ecx,eax\n" +
                                "\tmul ecx,eax\n" +
                                "\tsub ebx,ecx\n" +
                                "\tmov eax,ebx\n";
                        break;
                }
                return this.fil1.toString() +
                        "\tpush eax\n" +
                        this.fil2.toString()  +
                        "\tpop ebx\n" +
                        op;
            case COMPARATOR:
                String cp = "";
                switch (this.value.toString()) {
                    case "=" :
                        cp = "\tjz ";
                        break;
                    case "<" :
                        cp = "\tjle ";
                        break;
                    case "<=" :
                        cp = "\tjl ";
                        break;
                }
                return this.fil1.toString() +
                        "\tpush eax\n" +
                        this.fil2.toString()  +
                        "\tpop ebx\n" +
                        "\tsub eax,ebx\n" +
                        cp;
            case WHILE:
                idWhile++;
                return "debut_while_" + idWhile + ":\n" +
                        this.fil1.toString() + "faux_gt_" + idWhile + "\n" +
                        "\tmov eax,1\n" +
                        "\tjmp sortie_gt_" + idWhile + "\n" +
                        "faux_gt_" + idWhile + ":\n" +
                        "\tmov eax,0\n" +
                        "sortie_gt_" + idWhile + ":\n" +
                        "\tjz sortie_while_" + idWhile + "\n" +
                        this.fil2.toString() +
                        "\tjmp debut_while_" + idWhile + "\n" +
                        "sortie_while_" + idWhile + ":\n";
            case IF_THEN:
                idIf++;
                return "cond_if_" + idIf + ":\n" +
                        this.fil1.toString() + "sortie_if_" + idIf + "\n" +
                        "\tjmp then_if_" + idIf + "\n" +
                        "then_if_" + idIf + ":\n" +
                        this.fil2.toString() +
                        "\tjmp sortie_if_" + idIf + "\n" +
                        "sortie_if_" + idIf + ":\n";
            case IF_THEN_ELSE:
                idIf++;
                return "cond_if_" + idIf + ":\n" +
                        this.fil1.toString() + "else_if_" + idIf + "\n" +
                        "\tjmp then_if_" + idIf + "\n" +
                        this.fil2.toString() +
                        "sortie_if_" + idIf + ":\n";
            case THEN_ELSE:
                return "then_if_" + idIf + ":\n" +
                        this.fil1.toString() +
                        "\tjmp sortie_if_" + idIf + "\n" +
                        "else_if_" + idIf + ":\n" +
                        this.fil2.toString() +
                        "\tjmp sortie_if_" + idIf + "\n";
            default:
                return "";
        }
    }

    private void toArbre(String indent) {
        if (value != null) {
            if (fil1 != null) fil1.toArbre(indent + "    ");
            if(type == TypeNode.MOINS_UNAIRE)
                System.out.println(indent + "-" + value);
            else
                System.out.println(indent + value);
            if (fil2 != null)fil2.toArbre(indent + "    ");
        }
    }
}
