// link do problema: http://codeforces.com/problemset/problem/371/C

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    private static final int breadCode = 0;
    private static final int sausageCode = 1;
    private static final int cheeseCode = 2;
    private static String recipe;
    private static String kitchenIngredients;
    private static String prices;
    private static int[] recipeNumbers = { 0, 0, 0 };
    private static int[] numIngredients = { 0, 0, 0 };
    private static int[] ingredientsPrices = { 0, 0, 0 };
    private static long rubles;
    private static long numBurgers = 0;
    private static String binary;

    public static void main(String[] args) {
        scanData();
        formatScannedData();

        switchCall(); /* chama um switch cujo parametro e' a string de binario para tratar a situacao
                         da maneira mais especifica possivel - chama os mesmos metodos com parametros
                         diferentes (aplicacao de polimorfismo parametrico) */

        System.out.println(numBurgers); /* printa a soma do maximo de hamburgueres que podem ser feitos ao final da
                                           execucao */
    }

    private static void scanData() {
        Scanner scan = new Scanner(System.in);
        recipe = scan.nextLine(); // padrao: 1, 2 ou 3 letras ('B', 'S', 'C') sem espacos entre si
        kitchenIngredients = scan.nextLine(); // padrao: 3 inteiros separados por espaco
        prices = scan.nextLine(); // padrao: 3 inteiros separados por espaco
        rubles = scan.nextLong(); // padrao: um inteiro cujo valor maximo é 10^12
        scan.close();
    }

    private static void formatScannedData() {
        String[] aux = kitchenIngredients.split(" "); // split da entrada de ingredientes da cozinha
        numIngredients[breadCode] = Integer.parseInt(aux[breadCode]); // numero de paes na cozinha
        numIngredients[sausageCode] = Integer.parseInt(aux[sausageCode]); // numero de salsichas na cozinha
        numIngredients[cheeseCode] = Integer.parseInt(aux[cheeseCode]); // numero de queijos na cozinha
        aux = prices.split(" "); // split da entrada de precos de ingredientes
        ingredientsPrices[breadCode] = Integer.parseInt(aux[breadCode]); // preco do pao
        ingredientsPrices[sausageCode] = Integer.parseInt(aux[sausageCode]); // preco da salsicha
        ingredientsPrices[cheeseCode] = Integer.parseInt(aux[cheeseCode]); // preco do queijo

        for (int i = 0; i < recipe.length(); i++) { // contabilizacao dos ingredientes da receita
            if (recipe.charAt(i) == 'B') {
                recipeNumbers[breadCode] += 1;
            }
            if (recipe.charAt(i) == 'S') {
                recipeNumbers[sausageCode] += 1;
            }
            if (recipe.charAt(i) == 'C') {
                recipeNumbers[cheeseCode] += 1;
            }
        }

        binary = ""; // variavel chave para a otimizacao deste codigo; indica quais ingredientes serao usados
        for (int i = 0; i < 3; i++) { // concatenacao da string binary
            if (recipeNumbers[i] == 0) {
                binary += 0;
            } else {
                binary += 1;
            }
        }
    }

    private static void switchCall() { // switch para chamada de metodos polimorficos
        switch (binary) {
            case "001":
                checkResources(cheeseCode); // receita = somente queijo
                break;

            case "010":
                checkResources(sausageCode); // receita = somente salsicha
                break;

            case "100":
                checkResources(breadCode); // receita = somente pao
                break;

            case "011":
                checkResources(sausageCode, cheeseCode); // receita = salsicha e queijo
                break;

            case "101":
                checkResources(breadCode, cheeseCode); // receita = pao e queijo
                break;

            case "110":
                checkResources(breadCode, sausageCode); // receita = pao e salsicha
                break;

            case "111":
                checkResources(); // receita = pao, salsicha e queijo
                break;

            default:
                break;
        }
    }

    private static void checkResources() { // 3 ingredientes
        // checando estoque
        checkTheKitchen();

        // sobra da cozinha + valor na carteira (afim de zerar cozinha ou até o dinheiro acabar
        checkRemainsAndWallet();

        // checando quantos hamburgueres podem ser montados somente com ingredientes comprados
        checkRublesLeft();
    }

    private static void checkResources(int x) { // 1 ingrediente
        checkTheKitchen(x);
        checkRemainsAndWallet(x);
        checkRublesLeft();
    }

    private static void checkResources(int x, int y) { // 2 ingredientes
        checkTheKitchen(x, y);
        checkRemainsAndWallet(x, y);
        checkRublesLeft();
    }

    private static void checkTheKitchen() { /* verificacao dos valores dos ingredientes na cozinha, contabilizacao de
                                               hamburgueres a partir deles, decrescimento deles a partir do numero de
                                               hamburgueres montados */
        int min = 0;
        int minB = 0;
        int minS = 0;
        int minC = 0;
        ArrayList<Integer> list = new ArrayList<>();
        minB = numIngredients[breadCode] / recipeNumbers[breadCode];
        list.add(minB);
        minS = numIngredients[sausageCode] / recipeNumbers[sausageCode];
        list.add(minS);
        minC = numIngredients[cheeseCode] / recipeNumbers[cheeseCode];
        list.add(minC);
        Collections.sort(list); /* comparacao para definir o fator limitante (ingrediente menos numeroso da
                                   cozinha utilizado na receita, que define quantos hamburgueres se consegue
                                   fazer com os ingredientes da cozinha). nesse caso especifico, sempre estara
                                   no indice 0 */

        min = list.get(0); // min = fator limitante
        numBurgers = min; // numero de hamburgueres = fator limitante
        numIngredients[breadCode] -= numBurgers * recipeNumbers[breadCode]; // decrescimo do que foi gasto
        numIngredients[sausageCode] -= numBurgers * recipeNumbers[sausageCode];
        numIngredients[cheeseCode] -= numBurgers * recipeNumbers[cheeseCode];
    }

    private static void checkTheKitchen(int x) {
        numBurgers = numIngredients[x] / recipeNumbers[x];
        numIngredients[x] -= numBurgers * recipeNumbers[x];
    }

    private static void checkTheKitchen(int x, int y) {
        int min = Math.min(numIngredients[x] / recipeNumbers[x], numIngredients[y] / recipeNumbers[y]);
        numBurgers = min;
        numIngredients[x] -= numBurgers * recipeNumbers[x];
        numIngredients[y] -= numBurgers * recipeNumbers[y];
    }

    private static void checkRemainsAndWallet() { /* metodo que compara o que sobrou na cozinha com o que se pode
                                                     comprar para fazer mais hamburgueres */

        int[] remainingInKitchen = { numIngredients[breadCode], numIngredients[sausageCode],
                numIngredients[cheeseCode] }; // vetor local do que sobrou na cozinha

        boolean hasInKitchen = true; // boolean que indica se a cozinha ainda tem ingredientes
        boolean canBuy = true; // boolean que indica se ainda pode-se de comprar ingredientes
        int needBread = 0; // quantidade a ser comprada
        int needSausage = 0;
        int needCheese = 0;

        while (hasInKitchen) {
            needBread = recipeNumbers[breadCode] - remainingInKitchen[breadCode]; // calculo de quanto se precisa
            if (needBread <= 0) {
                needBread = 0; // o valor nunca pode ser menor que zero
            }
            needSausage = recipeNumbers[sausageCode] - remainingInKitchen[sausageCode];
            if (needSausage <= 0) {
                needSausage = 0;
            }
            needCheese = recipeNumbers[cheeseCode] - remainingInKitchen[cheeseCode];
            if (needCheese <= 0) {
                needCheese = 0;
            }

            boolean kitchenIsEmpty = false; // booleano de checagem se a cozinha ja foi esvaziada
            if (((remainingInKitchen[breadCode] == 0) && (remainingInKitchen[sausageCode] == 0))
                    && (remainingInKitchen[cheeseCode] == 0)) {
                kitchenIsEmpty = true;
                hasInKitchen = false; // fim do loop, caso a cozinha esteja vazia
            }

            /* if que garante que ao menos um dos valores de que se precisa e' maior que zero e que a cozinha
               ainda nao esta' vazia */
            if (((((needBread > 0) || (needSausage > 0)) || (needCheese > 0))) && (kitchenIsEmpty == false)) {

                canBuy = checkTheWallet(needBread, needSausage, needCheese); /* chamada de metodo que verifica se pode
                                                                                comprar mais ingredientes ou nao */

                if (canBuy) { // caso possa:
                    remainingInKitchen[breadCode] -= recipeNumbers[breadCode] - needBread; /* decrescimo do que se
                                                                                              gastou na cozinha em
                                                                                              relacao ao que foi
                                                                                              comprado */

                    remainingInKitchen[sausageCode] -= recipeNumbers[sausageCode] - needSausage;

                    remainingInKitchen[cheeseCode] -= recipeNumbers[cheeseCode] - needCheese;

                    numIngredients[breadCode] = remainingInKitchen[breadCode]; /* atribuicao do valor obtido localmente
                                                                                  'a variavel global */

                    numIngredients[sausageCode] = remainingInKitchen[sausageCode];

                    numIngredients[cheeseCode] = remainingInKitchen[cheeseCode];

                    numBurgers += 1; // acrescimo do numero de hamburgueres que podem ser feitos

                } else {
                    hasInKitchen = false; // fim do loop
                }
            } else {
                hasInKitchen = false; // fim do loop
            }
        }
    }

    private static void checkRemainsAndWallet(int x) {
        int remainingInKitchen = numIngredients[x];
        boolean hasInKitchen = true;
        boolean canBuy = true;
        int needX = 0;

        while (hasInKitchen) {
            needX = recipeNumbers[x] - remainingInKitchen;
            if (needX <= 0) {
                needX = 0;
            }

            boolean kitchenIsEmpty = false;
            if (remainingInKitchen == 0) {
                kitchenIsEmpty = true;
                hasInKitchen = false;
            }

            if ((needX > 0) && (kitchenIsEmpty == false)) {
                canBuy = checkTheWallet(needX, x);
                if (canBuy) {
                    remainingInKitchen -= recipeNumbers[x] - needX;
                    numIngredients[x] = remainingInKitchen;
                    numBurgers += 1;
                } else {
                    hasInKitchen = false;
                }
            } else {
                hasInKitchen = false;
            }
        }
    }

    private static void checkRemainsAndWallet(int x, int y) {
        int[] remainingInKitchen = { numIngredients[x], numIngredients[y] };
        boolean hasInKitchen = true;
        boolean canBuy = true;
        int needX = 0;
        int needY = 0;

        while (hasInKitchen) {
            needX = recipeNumbers[x] - remainingInKitchen[0];
            if (needX <= 0) {
                needX = 0;
            }
            needY = recipeNumbers[y] - remainingInKitchen[1];
            if (needY <= 0) {
                needY = 0;
            }

            boolean kitchenIsEmpty = false;
            if (((remainingInKitchen[0] == 0) && (remainingInKitchen[1] == 0))) {
                kitchenIsEmpty = true;
                hasInKitchen = false;
            }

            if ((((needX > 0) || (needY > 0))) && (kitchenIsEmpty == false)) {
                canBuy = checkTheWallet(needX, needY, x, y);
                if (canBuy) {
                    remainingInKitchen[0] -= recipeNumbers[x] - needX;
                    remainingInKitchen[1] -= recipeNumbers[y] - needY;
                    numIngredients[x] = remainingInKitchen[0];
                    numIngredients[y] = remainingInKitchen[1];
                    numBurgers += 1;
                } else {
                    hasInKitchen = false;
                }
            } else {
                hasInKitchen = false;
            }
        }
    }

    private static boolean checkTheWallet(int needBread, int needSausage, int needCheese) {
        /* verifica se pode-se comprar ingredientes necessarios a receita, complementares aos da cozinha - se puder,
           retorna true; senao, retorna false */

        boolean canBuy = true;
        int price = needBread * ingredientsPrices[breadCode] + needSausage * ingredientsPrices[sausageCode]
                + needCheese * ingredientsPrices[cheeseCode]; /* contabilizacao do preco com base nos ingredientes
                                                                 necessarios */

        if (rubles >= price) { // comparacao entre dinheiro e preco - condicao de retorno do metodo
            rubles -= price;
            return canBuy;
        } else {
            canBuy = false;
            return canBuy;
        }
    }

    private static boolean checkTheWallet(int needX, int x) {
        boolean canBuy = true;
        int price = needX * ingredientsPrices[x];
        if (rubles >= price) {
            rubles -= price;
            return canBuy;
        } else {
            canBuy = false;
            return canBuy;
        }
    }

    private static boolean checkTheWallet(int needX, int needY, int x, int y) {
        boolean canBuy = true;
        int price = needX * ingredientsPrices[x] + needY * ingredientsPrices[y];
        if (rubles >= price) {
            rubles -= price;
            return canBuy;
        } else {
            canBuy = false;
            return canBuy;
        }
    }

    private static void checkRublesLeft() { /* conta quantos hamburgueres se consegue fazer somente com ingredientes
                                               comprados, depois de nao ter mais nada na cozinha que satisfaca a
                                               receita */

        int burgerPrice = ((recipeNumbers[breadCode] * ingredientsPrices[breadCode])
                + (recipeNumbers[sausageCode] * ingredientsPrices[sausageCode])
                + (recipeNumbers[cheeseCode] * ingredientsPrices[cheeseCode]));

        if (rubles >= burgerPrice) {
            long howManyMoreCanBuy = rubles / burgerPrice;
            numBurgers += howManyMoreCanBuy;
        }
    }
}
