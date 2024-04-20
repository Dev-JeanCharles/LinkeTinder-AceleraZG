package org.jean.linketinder.View

import org.jean.linketinder.Entities.Candidate


class PrintOperationsView {

    static Candidate createCandidate(Scanner scanner) {

        println "Digite o nome do candidato: "
        String name = scanner.nextLine()

        println "Digite o email do candidato: "
        String email = scanner.nextLine()

        println "Digite o CPF do candidato: "
        String cpf = scanner.nextLine()

        println "Digite a idade do candidato: "
        int age = scanner.nextInt()
        scanner.nextLine()

        println "Digite o estado do candidato: "
        String state = scanner.nextLine()

        println "Digite o CEP do candidato: "
        String cep = scanner.nextLine()

        println "Digite a descrição do candidato: "
        String description = scanner.nextLine()

        println "Digite as competências do candidato (separadas por vírgula): "
        List<String> skills = parseSkills(scanner.nextLine())

        Candidate candidate = new Candidate(name, email, cpf, age, state, cep, description, skills as List<Skills>)
        return candidate
    }

    static Candidate updateCandidate(Scanner scanner) {

        print "Digite o CPF do candidato que deseja atualizar: "
        String cpf = scanner.nextLine()

        print "Digite o novo nome do candidato: "
        String name = scanner.nextLine()

        print "Digite o novo email do candidato: "
        String email = scanner.nextLine()

        print "Digite a nova idade do candidato: "
        int age = scanner.nextInt()
        scanner.nextLine()

        print "Digite o novo estado do candidato: "
        String state = scanner.nextLine()

        print "Digite o novo CEP do candidato: "
        String cep = scanner.nextLine()

        print "Digite a nova descrição do candidato: "
        String description = scanner.nextLine()

        print "Digite as novas competências do candidato (separadas por vírgula): "
        List<String> skills = parseSkills(scanner.nextLine())

        Candidate candidate = new Candidate(name, email, cpf, age, state, cep, description, skills as List<Skills>)
        return candidate
    }

    static String deleteCandidate(Scanner scanner) {
        print "Digite o CPF do candidato que deseja deletar: "
        return scanner.nextLine()
    }

    static void displayCandidateInfo(Candidate candidate) {
        println "Nome: ${candidate.name}\nEmail: ${candidate.email}\nCPF: ${candidate.cpf}\nIdade: ${candidate.age}\nEstado: ${candidate.state}\nCEP: ${candidate.cep}\nDescrição Pessoal: ${candidate.description}\n"

        if (!candidate.skillsList.empty) {
            println "Competências: ${candidate.skillsList.join(', ')}"
        } else {
            println "Nenhuma competência cadastrada para este candidato."
        }
        println ""
    }

    private static List<String> parseSkills(String skillsInput) {
        String[] skillsArray = skillsInput.split(",")
        List<String> skillsList = new ArrayList<>()
        for (String skill : skillsArray) {
            skillsList.add(skill.trim())
        }
        return skillsList
    }
}