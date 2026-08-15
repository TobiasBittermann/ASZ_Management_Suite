import {useEffect, useState} from "react";
import {loadInventories, loadMembers} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import InventoryAddEdit from "../../components/bierwart/InventoryAddEdit.jsx";
import {getMemberName} from "../../utils/namingUtils.jsx";

function InventoryPage() {
    const [inventories, setInventories] = useState([])
    const [members, setMembers] = useState([])
    const [selectedInventory, setSelectedInventory] = useState(null)
    const [isModalOpen, setIsModalOpen] = useState(false)

    useEffect(() => {
        loadMembers(setMembers);
    }, [])

    async function handleSaveInventory(inventory) {
        await saveEntity(inventory, "/inventories", loadInventories, setInventories);
    }

    async function handleDeleteInventory(id) {
        await deleteEntity(id, "/inventories", loadInventories, setInventories);
    }

    function handleEditClick(inventory) {
        setSelectedInventory(inventory);
        setIsModalOpen(true);
    }

    function handleAddClick() {
        setSelectedInventory(null);
        setIsModalOpen(true);
    }

    function startInventory() {

    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Inventuren
            </h3>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Start a new inventory"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

            {
                isModalOpen && (
                    <InventoryAddEdit
                        inventory={selectedInventory}
                        members={members}
                        onClose={() => setIsModalOpen(false)}
                        onSave={handleSaveInventory}/>
                )}


            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Datum</th>
                        <th className={"px-6 py-3"}>Member</th>
                        <th className={"px-6 py-3"}>Bemerkung</th>
                        <th className={"px-6 py-3"}>Beendet</th>
                        <th className={"px-6 py-3"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {inventories.map(inventory =>(
                        <tr key={inventory.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{inventory.id}</td>
                            <td className={"px-6 py-3"}>{inventory.date}</td>
                            <td className={"px-6 py-3"}>{getMemberName(members, inventory.memberId)}</td>
                            <td className={"px-6 py-3"}>{inventory.note}</td>
                            <td className={"px-6 py-3"}>{inventory.finished}</td>
                            <td>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit an inventory"}
                                    onClick={() => handleEditClick(inventory)}>
                                    <FiEdit3 />
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"delete-tip"}
                                    data-tooltip-content={"Delete an inventory"}
                                    onClick={() => handleDeleteInventory(inventory.id)}>
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
    )
}

export default InventoryPage;