import {useEffect, useState} from "react";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import VendorAddEdit from "../../components/bierwart/VendorAddEdit.jsx";
import {loadVendors} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";

function VendorsPage() {
    const [vendors, setVendors] = useState([])
    const [selectedVendor, setSelectedVendor] = useState(null)
    const [isModalOpen, setIsModalOpen] = useState(false)

    useEffect(() => {
        loadVendors(setVendors);
    }, [])

    async function handleSaveVendor(vendor) {
        await saveEntity(vendor, "/vendors", loadVendors, setVendors)
    }

    async function handleDeleteVendor(id){
        await deleteEntity(id, "/vendors", loadVendors, setVendors)
    }

    function handeEditClick(vendor){
        setSelectedVendor(vendor);
        setIsModalOpen(true);
    }

    function handleAddClick(){
        setSelectedVendor(null);
        setIsModalOpen(true);
    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Lieferanten
            </h3>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Add a new vendor"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

            {
                isModalOpen &&
                <VendorAddEdit
                    vendor={selectedVendor}
                    onClose={()=> setIsModalOpen(false)}
                    onSave={handleSaveVendor} />
            }

            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Name</th>
                        <th className={"px-6 py-3"}>Kontaktperson</th>
                        <th className={"px-6 py-3"}>Adresse</th>
                        <th className={"px-6 py-3"}>IBAN</th>
                        <th className={"px-6 py-3"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {vendors.map(vendor => (
                        <tr key={vendor.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{vendor.id}</td>
                            <td className={"px-6 py-3"}>{vendor.name}</td>
                            <td className={"px-6 py-3"}>{vendor.contactPerson}</td>
                            <td className={"px-6 py-3"}>{vendor.address}</td>
                            <td className={"px-6 py-3"}>{vendor.iban}</td>
                            <td>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit a member entry"}
                                    onClick={() => handeEditClick(vendor)}>
                                    <FiEdit3 />
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"delete-tip"}
                                    data-tooltip-content={"Delete a member entry"}
                                    onClick={() => handleDeleteVendor(vendor.id)}>
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

export default VendorsPage;