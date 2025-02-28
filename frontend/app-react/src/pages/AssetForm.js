import React, { useState } from 'react';
import './AssetForm.css';

export default function AssetForm() {
  const [formData, setFormData] = useState({
    type: 'buy', // Par défaut, le type est "achat"
    assetName: '',
    quantity: '',
    price: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Logique de soumission du formulaire
    console.log('Formulaire soumis:', formData);
  };

  return (
    <div className="asset-form-container">
      <h2>{formData.type === 'buy' ? 'Acheter un Actif' : 'Vendre un Actif'}</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>
            Type de transaction:
            <select name="type" value={formData.type} onChange={handleChange}>
              <option value="buy">Achat</option>
              <option value="sell">Vente</option>
            </select>
          </label>
        </div>
        <div className="form-group">
          <label>
            Nom de l'actif:
            <input
              type="text"
              name="assetName"
              value={formData.assetName}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <div className="form-group">
          <label>
            Quantité:
            <input
              type="number"
              name="quantity"
              value={formData.quantity}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <div className="form-group">
          <label>
            Prix:
            <input
              type="number"
              name="price"
              value={formData.price}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <button type="submit" className="submit-button">
          Soumettre
        </button>
      </form>
    </div>
  );
}
