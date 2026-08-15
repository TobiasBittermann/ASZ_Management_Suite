import {useEffect, useState} from "react";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi"
import {Tooltip} from "react-tooltip";
import DrinkAddEdit from "../../components/bierwart/DrinkAddEdit.jsx";
import {loadDrinks} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";

function DrinksPage() {
    const [drinks, setDrinks] = useState([]);
    const [selectedDrink, setSelectedDrink] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        loadDrinks(setDrinks);
    }, [])

    async function handleSaveDrink(drink){
        await saveEntity(drink, "/drinks", loadDrinks, setDrinks)
    }

    async function handleDeleteDrink(id){
        await deleteEntity(id, "/drinks", loadDrinks, setDrinks)
    }

    function handleEditClick(drink) {
        setSelectedDrink(drink);
        setIsModalOpen(true);
    }

    function handleAddClick() {
        setSelectedDrink(null);
        setIsModalOpen(true);
    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Getränkeliste
            </h3>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Add a new drink"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

            {
                isModalOpen && (
                    <DrinkAddEdit
                        drink={selectedDrink}
                        onClose={() => setIsModalOpen(false)}
                        onSave={handleSaveDrink}/>
                )}

            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Name</th>
                        <th className={"px-6 py-3"}>Einkaufspreis</th>
                        <th className={"px-6 py-3"}>Verkaufspreis</th>
                        <th className={"px-6 py-3"}>Faktor</th>
                        <th className={"px-6 py-3"}>Menge</th>
                        <th className={"px-6 py-3"}>Gesamtwert</th>
                        <th className={"px-6 py-3"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {drinks.map(drink => (
                        <tr key={drink.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{drink.id}</td>
                            <td className={"px-6 py-3"}>{drink.name}</td>
                            <td className={"px-6 py-3"}>{drink.purchasePrice}</td>
                            <td className={"px-6 py-3"}>{drink.sellingPrice}</td>
                            <td className={"px-6 py-3"}>{drink.factor}</td>
                            <td className={"px-6 py-3"}>{drink.amount}</td>
                            <td className={"px-6 py-3"}>{drink.totalValue}</td>
                            <td>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit a member entry"}
                                    onClick={() => handleEditClick(drink)}>
                                    <FiEdit3 />
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"delete-tip"}
                                    data-tooltip-content={"Delete a member entry"}
                                    onClick={() => handleDeleteDrink(drink.id)}>
                                    <FiTrash2 />
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <Tooltip id={"edit-tip"}/>
                <Tooltip id={"delete-tip"}/>
            </div>
        </div>
    );
}

export default DrinksPage;