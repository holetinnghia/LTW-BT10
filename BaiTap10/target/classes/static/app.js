async function gql(query, variables={}){
    const res = await fetch('/graphql',{
        method:'POST', headers:{'Content-Type':'application/json'},
        body: JSON.stringify({query, variables})
    });
    return res.json();
}

async function loadProductsSorted(){
    const q = `query{ productsSortedByPrice{ id title price quantity category{ name } } }`;
    const data = await gql(q);
    const list = document.getElementById('prod-list');
    list.innerHTML = (data.data.productsSortedByPrice||[]).map(p=>
        `<li>#${p.id} - ${p.title} - $${p.price} - ${p.category? p.category.name:''}</li>`
    ).join('');
}

async function loadProductsByCategory(){
    const cid = document.getElementById('cid').value;
    const q = `query($id:ID!){ productsByCategory(categoryId:$id){ id title price } }`;
    const data = await gql(q,{id: cid});
    const list = document.getElementById('prod-by-cat');
    list.innerHTML = (data.data.productsByCategory||[]).map(p=>
        `<li>${p.title} - $${p.price}</li>`
    ).join('');
}