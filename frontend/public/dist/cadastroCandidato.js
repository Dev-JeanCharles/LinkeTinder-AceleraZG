(()=>{"use strict";let e="",a=0,c="",t="",o="",n="",r=[],s="";const l=document.forms.namedItem("form1");null==l||l.addEventListener("submit",(l=>{var d;l.preventDefault(),function(e,a,c,t,o,n,r,s){return""!==e&&a>0&&""!==c&&""!==t&&""!==o&&""!==n&&s.length>0&&""!==r}(e,a,c,t,o,n,s,r)?(d={nome:e,idade:a,cpf:c,estado:t,cep:o,email:n,competencias:r,descricao:s},localStorage.setItem(d.cpf,JSON.stringify(d)),alert("Cadastro realizado com sucesso!"),window.location.href=`perfilCandidato.html?cpf=${encodeURIComponent(c)}`):alert("Por favor, preencha todos os campos obrigatórios.")})),["nome","idade","cpf","estado","cep","email","descricao"].forEach((r=>{const l=document.getElementById(r);null==l||l.addEventListener("change",(()=>{switch(r){case"nome":e=l.value;break;case"idade":a=parseInt(l.value,10);break;case"cpf":c=l.value;break;case"estado":t=l.value;break;case"cep":o=l.value;break;case"email":n=l.value;break;case"descricao":s=l.value}}))})),document.querySelectorAll('input[name="competencias[]"]').forEach((e=>{e.addEventListener("change",(()=>{e.checked?r.push(e.value):r=r.filter((a=>a!==e.value))}))}))})();