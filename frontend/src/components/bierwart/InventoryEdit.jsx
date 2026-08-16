import {useEffect, useState} from "react";
import {loadDrinks, loadInventoryEntries} from "../../utils/loadUtils.jsx";
import {getDrinkName} from "../../utils/namingUtils.jsx";

function InventoryEdit({inventory, onClose, onSave}) {
    const [inventoryEntries, setInventoryEntries] = useState([])
    const [drinks, setDrinks] = useState([])

    useEffect(() => {
        loadInventoryEntries(setInventoryEntries).then()
        loadDrinks(setDrinks).then()
    }, []);

    async function handleSubmit(event) {
        event.preventDefault();

        const currentEntries = inventoryEntries.filter(entry => entry.inventoryId === inventory.id)
        const allCounted = currentEntries.every(entry => entry.quantity != null);

        if (!allCounted) {
            alert("Noch nicht alle Getränke gezählt, Zwischenstand gespeichert.")
        }

        for (const entry of currentEntries) {
            await onSave(entry);
        }
        onClose();
    }

    return (
        <div className={"fixed inset-0 bg-black/50 flex items-center justify-center z-50"}>
            <div className={"bg-white rounded-2xl shadow-xl p-8 w-full max-w-6xl"}>

                <h2 className={"text-2xl font-bold text-gray-800 mb-6"}>
                    {inventory.note}
                </h2>

                //TODO: add inventory information properly

                <form
                    className={"grid grid-cols-[auto_1fr] items-center gap-x-4 gap-y-4"}
                    onSubmit={handleSubmit}>

                    <table className={"min-w-full bg-white text-sm text-left"}>
                        <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                        <tr>
                            <th className={"px-6 py-3"}>Id</th>
                            <th className={"px-6 py-3"}>Getränk</th>
                            <th className={"px-6 py-3"}>Anfangsbestand</th>
                            <th className={"px-6 py-3"}>Gezählter Bestand</th>
                            <th className={"px-6 py-3"}>Schwund</th>
                            <th className={"px-6 py-3"}>Stückpreis</th>
                            <th className={"px-6 py-3"}>Gesamtwert</th>
                            <th className={"px-6 py-3"}>Schwundwert</th>
                        </tr>
                        </thead>
                        <tbody className={"divide-y divide-gray-100"}>
                        {inventoryEntries
                            .filter(entry => entry.inventoryId === inventory.id)
                            .map(entry => (
                                <tr key={entry.id}>
                                    <td className={"px-6 py-3"}>{entry.id}</td>
                                    <td className={"px-6 py-3"}>{getDrinkName(drinks, entry.drinkId)}</td>
                                    <td className={"px-6 py-3"}>{entry.initialQuantity}</td>
                                    <td>
                                        <input type={"number"}
                                               value={entry.quantity ?? ""}
                                               onChange={event => {
                                                   const value = event.target.value;
                                                   const quantity = value === "" ? null : Number(value);

                                                   setInventoryEntries(entries =>
                                                       entries.map(e =>
                                                           e.id === entry.id
                                                               ? {
                                                                   ...e,
                                                                   quantity: quantity
                                                               }
                                                               : e));
                                               }}/>
                                    </td>
                                    <td className={"px-6 py-3"}>{entry.shrinkage}</td>
                                    <td className={"px-6 py-3"}>{entry.unitValue}</td>
                                    <td className={"px-6 py-3"}>{entry.totalValue}</td>
                                    <td className={"px-6 py-3"}>{entry.shrinkageValue}</td>
                                </tr>
                            ))
                        }
                        </tbody>
                    </table>

                    <div className={"col-span-2 flex justify-end gap-3 mt-2"}>
                        <button
                            className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-6 py-2 m-1 transition"}
                            type={"submit"}>
                            Save
                        </button>
                        <button
                            className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-6 py-2 m-1 transition"}
                            type={"button"} onClick={onClose}>
                            Close
                        </button>
                    </div>

                </form>

            </div>
        </div>
    )
}

export default InventoryEdit;