import { Candidato } from "../Models/Candidato";
import { aplicarMascara } from "../Utils/mascarar";

export function construirCandidato(candidato: Candidato): string {
    const { nome, email, cpf, competencias, descricao } = candidato;

    const emailMascarado = aplicarMascara(email, 'email');
    const cpfMascarado = aplicarMascara(cpf, 'cpf');
    const nomeMascarado = aplicarMascara(nome, 'nome');

    const competenciasFormatadas = formatarCompetencias(competencias);

    return `
        <div class="candidato">
            <h2>${nomeMascarado}</h2>
            <p>Email: ${emailMascarado}</p>
            <p>CPF: ${cpfMascarado}</p>
            <p>Competências: ${competenciasFormatadas}</p>
            <p>Descrição: ${descricao}</p>
        </div>
    `;
}

function formatarCompetencias(competencias: string[]): string {
    if (competencias.length === 0) return '';
    if (competencias.length === 1) return competencias[0];

    const competenciasFormatadas = competencias.slice(0, -1).join(", ");
    const ultimaCompetencia = competencias[competencias.length - 1];
    
    return `${competenciasFormatadas}, ${ultimaCompetencia}`;
}
