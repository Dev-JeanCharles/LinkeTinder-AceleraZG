import { Candidato } from "../Models/Candidato";
import { Empresa } from "../Models/Empresa";
import {mascararNome, mascararEmail, mascararCNPJ} from "../Utils/mascarar";

function listarLocalStorage(): (Candidato | Empresa)[] {
    const dadosLocalStorage: (Candidato | Empresa)[] = [];

    for (let i = 0; i < localStorage.length; i++) {
        const chave = localStorage.key(i);
        if (chave) {
            const valor = localStorage.getItem(chave);
            if (valor) {
                try {
                    const cadastro: Candidato | Empresa = JSON.parse(valor);
                    dadosLocalStorage.push(cadastro);
                } catch (error) {
                    console.error(`Erro ao analisar o valor para a chave ${chave}:`, error);
                    continue;
                }
            }
        }
    }
    return dadosLocalStorage;
}

function obterDadosCandidato(): Candidato | null {
    const cpfParam = new URLSearchParams(window.location.search).get('cpf');
    if (!cpfParam) return null;

    const candidatoJSON = localStorage.getItem(cpfParam);
    if (!candidatoJSON) return null;
    return JSON.parse(candidatoJSON);
}



function preencherPerfilCandidato(candidato: Candidato): void {
    const nomeElement = document.querySelector('.nome');
    if (nomeElement) nomeElement.textContent = candidato.nome;

    const emailElement = document.querySelector('.email');
    if (emailElement) emailElement.textContent = candidato.email;
    const residenciaElement = document.querySelector('ul.dadosGeograficos');
    if (residenciaElement) {
        residenciaElement.children[0].textContent = candidato.estado + ',';
        residenciaElement.children[1].textContent = candidato.cep;
    }
    const idadeElement = document.querySelector('ul.dadosDemograficos > li:nth-child(1)');
    if (idadeElement) idadeElement.textContent = candidato.idade.toString() + " anos";
    const cpfElement = document.querySelector('ul.dadosDemograficos > li:nth-child(2)');
    if (cpfElement) cpfElement.textContent = candidato.cpf;
    const competenciasElement = document.querySelector('ul.dadosDemograficos > li:nth-child(3) > ul');
    if (competenciasElement) {
        competenciasElement.innerHTML = candidato.competencias.map((competencia, index, array) => {
            if (index < array.length - 1) {
                return `<li class="me-1">${competencia},</li>`;
            } else {
                return `<li class="me-1">${competencia}</li>`;
            }
        }).join('');
    }
    const descricaoElement = document.querySelector('ul.dadosDemograficos > li:nth-child(4) > div');
    if (descricaoElement) descricaoElement.textContent = candidato.descricao;
}

function exibirVagasEmpresas(): void {
    const listaEmpresasElement = document.getElementById("lista-empresas");
    if (listaEmpresasElement) {
        listaEmpresasElement.innerHTML = '';

        const dadosLocalStorage = listarLocalStorage();

        const empresas = dadosLocalStorage.filter((cadastro) => 'cnpj' in cadastro) as Empresa[];

        empresas.forEach(empresa => {
            const card = `
                <div class="col">
                    <div class="card">
                        <div class="card-body">
                        <h5 class="card-title">${mascararNome(empresa.nome)}</h5>
                            <p class="card-text">CNPJ: ${mascararCNPJ(empresa.cnpj)}</p>
                            <p class="card-text">Estado: ${empresa.estado}</p>
                            <p class="card-text">CEP: ${empresa.cep}</p>
                            <p class="card-text">Email: ${mascararEmail(empresa.email)}</p>
                            <p class="card-text">Competências: ${empresa.competencias.slice(0, -1).join(", ")}${empresa.competencias.length > 1 ? ',' : ''} ${empresa.competencias.slice(-1)}</p>   
                            <p class="card-text">Descrição: ${empresa.descricao}</p>
                            </div>
                        </div>
                    </div>
                `;
                listaEmpresasElement.innerHTML += card;
            });
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        const candidato = obterDadosCandidato();
    
        if (candidato) {
            preencherPerfilCandidato(candidato);
            exibirVagasEmpresas();
        } else {
            console.error('Candidato não encontrado.');
        }
    });