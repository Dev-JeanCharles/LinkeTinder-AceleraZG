(()=>{"use strict";document.addEventListener("DOMContentLoaded",(()=>{const e=function(){const e=new URLSearchParams(window.location.search).get("cnpj");if(!e)return null;const t=localStorage.getItem(e);return t?JSON.parse(t):null}();e?function(e){const t=document.querySelector(".nome");t&&(t.textContent=e.nome);const n=document.querySelector(".email");n&&(n.textContent=e.email);const o=document.querySelector("ul.dadosGeograficos");o&&(o.children[0].textContent=`${e.estado},`,o.children[1].textContent=e.cep);const c=document.querySelector(".cnpj");c&&(c.textContent=e.cnpj);const r=document.querySelector(".competencias");r&&(r.innerHTML=e.competencias.map(((e,t,n)=>`<li class="me-1">${e}${t<n.length-1?",":""}</li>`)).join(""));const l=document.querySelector(".descricao");l&&(l.textContent=e.descricao);const s=document.querySelector(".cnpj-label");s&&(s.textContent="CNPJ:");const a=document.querySelector(".competencias-label");a&&(a.textContent="Competências:");const u=document.querySelector(".descricao-label");u&&(u.textContent="Sobre:")}(e):console.error("Empresa não encontrada.")}))})();