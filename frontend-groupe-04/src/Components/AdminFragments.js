import callAPI from "../utils/api";

export function getUserFragmentShort(monEtat, monType, user,sales) {
    let page = `
        <div class="row justify-content-center align-items-center h-100 border border-dark rounded my-4">
            <div class="container-fluid">
                <div class="row m-2">
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Prénom : ${user.firstName}" readonly>
                    </div>
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Nom : ${user.name}" readonly>
                    </div>
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Pseudo : ${user.pseudo}" readonly>
                    </div>
                </div>
                <div class="row m-2">
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Email : ${user.email}" readonly>
                    </div>
                    <div class="col mx-1">
                        <input class="form-control-plaintext " type="text" placeholder="Date d'inscription : TODO" readonly>
                    </div>
                </div>
                <div class="row m-2">
                    <!-- récuperer tous les meubles pour compter combien vendus et achetés par l'utilisateur courant -->
                    <div class="col">Nombre de meubles achetés : ${sales.length}</div>
                    <button class="btn btn-info" type="button" data-toggle="collapse" data-target="#collapseExample${user.pseudo}" aria-expanded="false" aria-controls="collapseExample${user.pseudo}">
                        Plus d'info
                    </button>
                </div>
                <div class="row justify-content-center align-items-center">
                    <div class="collapse w-75 pb-3" id="collapseExample${user.pseudo}">
                        
                        <p class="align-items-left">Meubles Achetes :</p>
                        <!-- debut meuble -->
                        `;
    if(sales.length>0){
        for(let i = 0; i<sales.length; i++){

            callAPI(
                "/api/furnitures/"+sales[i].idFurniture,
                "GET",
                undefined,
                undefined
            ).then((response) => {
                page += `
                    <div class="row justify-content-center align-items-center h-100">
                        <form class="container-fluid pt-3 d-flex">
                            <div class="col-3 col-sm-3 col-md-4 col-lg-3 furnitures-photo-border">
                                <img id="image_test" src="" alt="photo_meuble" class="img-fluid">
                            </div>
                            <div class="col-5 col-sm-5 col-md-6 col-lg-7 furnitures-description-border">
                                <h5 class="px-2 pt-3">Description :</h5>
                                <p class="px-2"> ${response.description}</p>
            
                                <h5 class="px-2 pt-3">Type : Type</h5>
            
                                <h5 class="px-2 pt-3">État : Etat</h5>
                            </div>
                            <div class="col-4 col-sm-4 col-md-2 col-lg-2 furnitures-price-border text-center">
                                <div>
                                    <h5 class="px-2 pt-3">Prix de vente:</h5>
                                    <p class="px-2 text-wrap">${response.sellingPrice}€</p>
                                </div>
                            </div>
                        </form>
                    </div>
                `;
            });
        }
        
    } else {
        page +=`<p>Aucun meubles n'a été acheté par cet utilisateur</p>`
    }
    page +=`
                        <!-- fin meuble -->
                    </div>
                </div>
            </div>
        </div>
    `;
    return page;
};

export function getUserFragmentLong(user) {
    return `
        <div class="row justify-content-center align-items-center h-100 border border-dark rounded my-4">
            <div class="container-fluid">
                <div class="row">
                    <div class="col">${user.firstName}</div>
                    <div class="col">${user.name}</div>
                    <div class="col">${user.pseudo}</div>
                </div>
                <div class="row">
                    <div class="col">${user.email}</div>
                    <div class="col">date d'inscription</div>
                </div>
                <div class="row">
                    <!-- récuperer tous les meubles vendus par l'utilisateur courant -->
                    <div class="col">Nombre de meubles vendus : X</div>
                </div>
                <div class="row">
                    <!-- récuperer tous les meubles achetés par l'utilisateur courant -->
                    <div class="col">Nombre de meubles achetés : X</div>
                </div>
            </div>
        </div>
    `;
};

export function getDefaultAdminPage() {
    return `
    <div class="container-fluid h-100 text-dark">
        <div class="row justify-content-center align-items-center">
            <h4 id="pageTitle" class="pt-3">Panel d'administration</h4>
        </div>
        <hr/>
        <nav>
            <div class="nav nav-tabs nav-fill" id="nav-tab" role="tablist">
                <a class="nav-link active" id="nav-general-tab" data-toggle="tab" href="#nav-general" role="tab" aria-controls="nav-general" aria-selected="true">Général</a>
                <a class="nav-link" id="nav-user-tab" data-toggle="tab" href="#nav-user" role="tab" aria-controls="nav-user" aria-selected="false">Utilisateurs</a>
                <a class="nav-link" id="nav-furniture-tab" data-toggle="tab" href="#nav-furniture" role="tab" aria-controls="nav-furniture" aria-selected="false">Meubles</a>
            </div>
        </nav>
        <div class="tab-content" id="nav-tabContent">
            <div class="tab-pane fade show active" id="nav-general" role="tabpanel" aria-labelledby="nav-general-tab">
                <div class="spinner-border" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
            <div class="tab-pane fade" id="nav-user" role="tabpanel" aria-labelledby="nav-user-tab">
                <div class="spinner-border" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
            <div class="tab-pane fade" id="nav-furniture" role="tabpanel" aria-labelledby="nav-furniture-tab">
                <div class="spinner-border" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
        </div>
    </div>
`;
}
