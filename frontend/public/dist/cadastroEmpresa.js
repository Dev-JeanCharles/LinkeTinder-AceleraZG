(()=>{"use strict";var e={712:(e,s,t)=>{Object.defineProperty(s,"__esModule",{value:!0}),s.Empresa=void 0;const a=t(231);class o extends a.Usuario{constructor(e,s,t,a,o,c,r,i){super(e,o,c,s,r,i),this.nome=e,this.email=s,this.cnpj=t,this.pais=a,this.estado=o,this.cep=c,this.descricao=r,this.competencias=i}getAll(){return{nome:this.nome,email:this.email,cnpj:this.cnpj,pais:this.pais,estado:this.estado,cep:this.cep,descricao:this.descricao,competencias:this.competencias}}}s.Empresa=o},231:(e,s)=>{Object.defineProperty(s,"__esModule",{value:!0}),s.Usuario=void 0,s.Usuario=class{constructor(e,s,t,a,o,c){this.nome=e,this.estado=s,this.cep=t,this.email=a,this.descricao=o,this.competencias=c}}},253:(e,s)=>{Object.defineProperty(s,"__esModule",{value:!0}),s.DTOEmpresa=void 0,s.DTOEmpresa=class{add(e){let s=this.get();s.push(e),localStorage.setItem("empresas",JSON.stringify(s))}get(){const e=localStorage.getItem("empresas");return e?JSON.parse(e):[]}}}},s={};function t(a){var o=s[a];if(void 0!==o)return o.exports;var c=s[a]={exports:{}};return e[a](c,c.exports,t),c.exports}(()=>{const e=t(712),s=t(253);let a,o,c,r,i,n,l,d=[];const p=document.forms.namedItem("form2");null==p||p.addEventListener("submit",(t=>{if(t.preventDefault(),function(){let e=!0;const s=/^[^\s@]+@(?!.*\d)[^\s@]+\.[^\s@]+$/;return["nome","email","cnpj","pais","estado","cep","descricao"].forEach((t=>{const a=document.getElementById(t);a&&""!==a.value.trim()?"email"===t&&(s.test(a.value)||(e=!1,alert("O e-mail inserido não é válido. Por favor, verifique e tente novamente."))):e=!1})),e}()){const t=new e.Empresa(a,o,c,r,i,n,l,d);(new s.DTOEmpresa).add(t),alert("Cadastro realizado com sucesso!"),window.location.href=`perfilEmpresa.html?cnpj=${encodeURIComponent(c)}`}else alert("Por favor, preencha todos os campos obrigatórios.")})),["nome","email","cnpj","pais","estado","cep","descricao"].forEach((e=>{const s=document.getElementById(e);null==s||s.addEventListener("change",(()=>{switch(e){case"nome":a=s.value;break;case"email":o=s.value;break;case"cnpj":c=s.value;break;case"pais":r=s.value;break;case"estado":i=s.value;break;case"cep":n=s.value;break;case"descricao":l=s.value}}))})),document.querySelectorAll('input[name="competencias[]"]').forEach((e=>{e.addEventListener("change",(()=>{e.checked?d.push(e.value):d=d.filter((s=>s!==e.value))}))}))})()})();