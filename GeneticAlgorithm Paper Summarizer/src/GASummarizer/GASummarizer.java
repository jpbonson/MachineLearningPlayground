package GASummarizer;

import java.util.ArrayList;
import java.util.Collections;

public class GASummarizer
{
    final int INITIAL_POPULATION = 48; // must be a multiple of 4
    final int GENERATIONS = 300;
    final int BREED_CATEGORIES_SIZE = 10;

    private FitnessFunction fitnessFunction;
    
    public GASummarizer(FitnessFunction fitnessFunction)
    {
        this.fitnessFunction = fitnessFunction;
    }
    
    // Basic genetic Algorithm Procedure (http://en.wikipedia.org/wiki/Genetic_algorithm)
    public Summary run()
    {
        // 1. Choose the initial population of individuals
        // 2. Evaluate the fitness of each individual in that population
        ArrayList<Summary> population = new ArrayList<>();
        Summary m;
        for(int i = 0; i < INITIAL_POPULATION; i++)
        {
            m = new Summary(fitnessFunction.getSentencesQuantity());
            m.setFitness(fitnessFunction.calculateFitness(m, population));
            population.add(m);
        }
        
        ArrayList<Summary> children;
        ArrayList<Summary> randoms;
        ArrayList<Summary> temp;
        ArrayList<Summary> temp2;
        // 3. Repeat on this generation until termination (time limit, sufficient fitness achieved, etc.):
        for(int i = 0; i < GENERATIONS; i++)
        {
            for(Summary sum: population)
                sum.setFitness(fitnessFunction.calculateFitness(sum, population));
            
            // 3.1. Select the best-fit individuals for reproduction
            ArrayList<Summary> bestFit = selectBestFit(population);
            
            /*temp2 = new ArrayList<>();
            temp2.addAll(bestFit);
            temp2.addAll(population);
            for(int j = 0; j < INITIAL_POPULATION/8; j++)
            {
                m = new Summary(fitnessFunction.getSentencesQuantity());
                m.setFitness(fitnessFunction.calculateFitness(m, temp2));
                Summary choosen2 = Collections.min(bestFit);
                bestFit.remove(choosen2);
                bestFit.add(m);
                temp2.add(m);
            }*/

            // 3.2. Breed new individuals through crossover and mutation operations to give birth to offspring
            children = breed(bestFit); // crossover and mutate children

            // avoid early convergence
            /*randoms = new ArrayList<>();
            temp = new ArrayList<>();
            temp.addAll(children);
            temp.addAll(population);
            for(int j = 0; j < INITIAL_POPULATION/2; j++)
            {
                m = new Summary(fitnessFunction.getSentencesQuantity());
                m.setFitness(fitnessFunction.calculateFitness(m, temp));
                randoms.add(m);
                temp.add(m);
            }*/
            
            // 3.3. Evaluate the individual fitness of new individuals
            // 3.4. Replace least-fit population with new individuals
            replace(population, children);//, randoms);
        }

        Summary choosen = Collections.max(population);
        //for(Summary test: population)
        //    System.out.println(test);
        return choosen;
    }

    public ArrayList<Summary> breed(ArrayList<Summary> bestFit)
    {
        ArrayList<Summary> childs = new ArrayList<>();

        int quant_categories = (fitnessFunction.getSentencesQuantity()-1)/BREED_CATEGORIES_SIZE;
        int last_categorie_size = (fitnessFunction.getSentencesQuantity())%BREED_CATEGORIES_SIZE;
        if(last_categorie_size > 0)
            quant_categories++;

        boolean[] newPath;
        int size;
        Summary child1;
        Summary child2;
        int num;
        for(int i = 0; i < bestFit.size(); i+=2)
        {
            num = (int)(Math.random()*quant_categories);
            if(num == quant_categories-1)
                size = last_categorie_size;
            else
                size = BREED_CATEGORIES_SIZE;
            Summary parent1 = bestFit.get(i);
            Summary parent2 = bestFit.get(i+1);
            newPath = new boolean[fitnessFunction.getSentencesQuantity()];
            for(int j = 0; j < fitnessFunction.getSentencesQuantity(); j++)
                newPath[j] = parent1.actives[j];
            for(int k = 0; k < size; k++)
                newPath[k+num*BREED_CATEGORIES_SIZE] = parent2.actives[k+num*BREED_CATEGORIES_SIZE];
            child1 = new Summary(newPath);
            newPath = new boolean[fitnessFunction.getSentencesQuantity()];
            for(int j = 0; j < fitnessFunction.getSentencesQuantity(); j++)
                newPath[j] = parent2.actives[j];
            for(int k = 0; k < size; k++)
                newPath[k+num*BREED_CATEGORIES_SIZE] = parent1.actives[k+num*BREED_CATEGORIES_SIZE];
            child2 = new Summary(newPath);
            
            // correct children with wrong extraction rate
            int num2;
            boolean temp;
            while(child1.activatedSentences() != Summary.SENTENCE_EXTRACTION ||
                    child2.activatedSentences() != Summary.SENTENCE_EXTRACTION)
            {
                Summary child_less, child_more;
                if(child1.activatedSentences() < Summary.SENTENCE_EXTRACTION)
                {
                    child_less = child1;
                    child_more = child2;
                }
                else
                {
                    child_less = child2;
                    child_more = child1;
                }
                
                num2 = (int)(Math.random()*size);
                while(child_less.actives[num2+num*BREED_CATEGORIES_SIZE])
                {
                    num2 = (int)(Math.random()*size);
                }

                temp = child_less.actives[num2+num*BREED_CATEGORIES_SIZE];
                child_less.actives[num2+num*BREED_CATEGORIES_SIZE] = child_more.actives[num2+num*BREED_CATEGORIES_SIZE];
                child_more.actives[num2+num*BREED_CATEGORIES_SIZE] = temp;
            }
            
            // mutate children
            child1 = mutate_children(child1);
            child2 = mutate_children(child2);
            
            child1.setFitness(fitnessFunction.calculateFitness(child1, bestFit));
            child2.setFitness(fitnessFunction.calculateFitness(child2, bestFit));
            
            childs.add(child1);
            childs.add(child2);
        }
        bestFit.addAll(childs);

        return bestFit; // parents + childrens
    }
    
    public Summary mutate_children(Summary child)
    {
        boolean mutated = false;
        int size = fitnessFunction.getSentencesQuantity();
        int index;
        int choice;
        while(!mutated)
        {
            index = (int)(Math.random()*size);
            choice = (int)(Math.random()*2);
            if(choice == 0 && index+1 < size && child.actives[index] == true && child.actives[index+1] == false)
            {
                child.actives[index] = false;
                child.actives[index+1] = true;
                mutated = true;
            }
            if(choice == 1 && index-1 >= 0 && child.actives[index] == true && child.actives[index-1] == false)
            {
                child.actives[index] = false;
                child.actives[index-1] = true;
                mutated = true;
            }
        }
        return child;
    }
    
    public ArrayList<Summary> replace(ArrayList<Summary> oldpop, ArrayList<Summary> newpop)//, ArrayList<Summary> newrandoms)
    {
        for(int i = 0; i < INITIAL_POPULATION/2; i++)
            oldpop.add(newpop.get(i));
        //for(int i = 0; i < INITIAL_POPULATION/2; i++)
        //    oldpop.add(newrandoms.get(i));

        Summary m;
        for(int i = 0; i < oldpop.size(); i++)
            for(int j = i+1; j < oldpop.size(); j++)
                if(oldpop.get(i).isEqualTo(oldpop.get(j)))
                {
                    oldpop.remove(oldpop.get(j));
                    m = new Summary(fitnessFunction.getSentencesQuantity());
                    m.setFitness(fitnessFunction.calculateFitness(m, oldpop));
                    oldpop.add(m);
                }
        
        for(int i = 0; i < INITIAL_POPULATION/2; i++)//+INITIAL_POPULATION/2; i++)
            oldpop.remove(Collections.min(oldpop));
        
        
        
        return oldpop;
    }

    public ArrayList<Summary> selectBestFit(ArrayList<Summary> population)
    {
        ArrayList<Summary> bestFit = new ArrayList<>();
        for(int i = 0; i < INITIAL_POPULATION/2; i++)
            bestFit.add(population.get(i));

        ArrayList<Summary> test = new ArrayList<>();
        for(int i = INITIAL_POPULATION/2; i < INITIAL_POPULATION; i++)
            test.add(population.get(i));

        Summary min;
        for(int j = 0; j < INITIAL_POPULATION/2; j++)
        {
            min = Collections.min(bestFit);
            for(int i = 0; i < test.size(); i++)
                if(test.get(i).compareTo(min) == 1)
                {
                    bestFit.remove(min);
                    bestFit.add(test.get(i));
                    test.remove(i);
                    break;
                }
        }

        return bestFit;
    }
}
